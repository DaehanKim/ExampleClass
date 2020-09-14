import matplotlib.pyplot as plt
import numpy as np 
import time
from matplotlib import rc, patches
from scipy.interpolate import make_interp_spline, BSpline

# default settings
def set_rc():
	rc('text', usetex=True)
	rc('font', family="Times New Roman")
	rc('font', size=20)

FONT_DICT = lambda: {
		'color':  'black',
		'weight': 'normal',
		'horizontalalignment': 'center',
		'verticalalignment' : 'center',
		'size':12}

def PT_NAMES(custom_pts=None) : 
	if custom_pts is None:
		return 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
	return custom_pts

ANGLE_DEFAULT_LENGTH = lambda: 0.2

class Line:
	def __init__(self):
		pass 

	@staticmethod
	def draw(_from, _to):
		return plt.plot([_from[0], _to[0]], [_from[1], _to[1]], 'k', zorder=2)

	@staticmethod
	def draw_angle(A, O, B, ax = None):
		def get_angle_of_vector(vector) :
			length = (vector**2).sum()**0.5
			angle = np.arccos(vector[0] / length) * 180 / np.pi
			angle *= (-1)**(vector[1] <= 0) # determine direction of angle
			return angle

		length = ((O - A)**2).sum()**0.5
		theta1 = get_angle_of_vector(A - O)
		theta2 = get_angle_of_vector(B - O)

		# print("theta1 : {}, theta2 : {}".format(theta1, theta2))
		angle_calibration = 0.6 if theta2-theta1 < 30 else 0

		arc = patches.Arc(xy = O, width = length*2+angle_calibration, height = length*2+angle_calibration, angle = 0, theta1 = theta1, theta2 = theta2)
		if ax is None:
			ax = plt.axes(label='main')
		ax.add_patch(arc)
		return arc

	@staticmethod
	def draw_right_angle(A, O, B):
		C = B + (A-O)
		return plt.plot([A[0], C[0], B[0]], [A[1], C[1], B[1]], 'k', zorder=1, linewidth=1)

class Shape:
	def __init__(self, pts, custom_point_names=None):
		self.pts = pts 
		self.lines = []
		self.len_pos = {}
		self.ax = None
		self.custom_point_names = custom_point_names

	def get_center_of_gravity(self):
		return np.array(self.pts).mean(0)

	def get_line_center(self):
		centers = np.array([line.get_xydata().mean(0) for line in self.lines])
		return centers

	def draw_shape(self):
		self.pts += (self.pts[0],)
		for i in range(len(self.pts)-1):
			self.lines.append(Line.draw(self.pts[i], self.pts[i+1])[0])

	def mark_points(self):
		center = np.array(self.pts).mean(0)
		for i, coord in enumerate(self.pts):
			coord = np.array(coord)
			offset = (coord-center)*0.15
			coord += offset
			plt.text(*coord, PT_NAMES(self.custom_point_names)[i], fontdict = FONT_DICT())

	def draw(self, ax=None):
		if ax is not None: self.ax = ax
		self.mark_points()
		self.draw_shape()

	def set_line_color(self, idx, color = "#4283FF"):
		self.lines[idx].set_color(color)

	def mark_line_length(self, idx, _len=None):
		if _len is None:  
			pts = self.lines[idx].get_xydata()
			line_len = ((pts[0]-pts[1])**2).sum()**0.5
			_len = '{:.2f}'.format(line_len)

		# display length string above lines
		cog = self.get_center_of_gravity()
		centers = self.get_line_center()

		offset = (centers - cog)*0.5
		self.len_pos[idx] = centers[idx] + offset[idx]
		plt.text(*self.len_pos[idx], _len, fontdict = FONT_DICT())

	@staticmethod
	def draw_length_aid_line(pts):
		# pts : ndarray (N x 2)
		def is_sorted(arr): 
			for i in range(arr.shape[0]-1):
				if arr[i] > arr[i+1] : return False
			return True
		def is_inverse_sorted(arr):
			for i in range(arr.shape[0]-1):
				if arr[i] < arr[i+1] : return False
			return True

		if is_inverse_sorted(pts[:, 0]) or is_inverse_sorted(pts[:, 1]): 
			pts = np.flip(pts, axis=0)
		axis = 0 if is_sorted(pts[:,0]) else 1
		x_coord , y_coord = pts[:, axis], pts[:, 1-axis]
		aid_line = make_interp_spline(x_coord, y_coord, k=2)
		x_ranges = [(x_coord[0], x_coord[1]-0.4), (x_coord[1]+0.4, x_coord[-1])]

		for _range in x_ranges:
			x = np.linspace(*_range, 300)
			data = [aid_line(x), x] if axis == 1 else [x, aid_line(x)]
			plt.plot(*data, linestyle="dotted", color="gray")

	def mark_length_aid_line(self, idx):
		pts = self.lines[idx].get_xydata()
		try:
			pts = np.insert(pts, 1, self.len_pos[idx], axis=0)
		except keyError:
			print("Need to call `mark_line_length` first.")
		Shape.draw_length_aid_line(pts)

	def mark_angle(self, idx, is_right_angle=False):
		# line containing pts[idx]
		line_1, line_2 = self.lines[idx-1], self.lines[idx]

		# angle start position : minimum of default_size, 0.7*(min length)

		# print("line1 : {}, pts[0] : {}".format(line_1.get_xydata(), line_1.get_xydata()[0]))
		# print("line2 : {}".format(line_2.get_xydata()))
		line_length_1 = line_1.get_xydata()[1] - line_1.get_xydata()[0]
		line_length_1 = (line_length_1**2).sum()**0.5
		# print('length of line 1 : {}'.format(line_length_1))
		line_length_2 = line_2.get_xydata()[1] - line_2.get_xydata()[0]
		line_length_2 = (line_length_2**2).sum()**0.5

		angle_start_length = min(ANGLE_DEFAULT_LENGTH(), 0.2*line_length_1, 0.2*line_length_2)

		# print("angle_start_length:{}".format(angle_start_length))

		# get position on each line
		line_1_start_pt = line_1.get_xydata()[1]
		line_1_end_pt = line_1.get_xydata()[0]
		line_2_start_pt = line_2.get_xydata()[0]
		line_2_end_pt = line_2.get_xydata()[1]
		ratio_1 = angle_start_length/line_length_1
		ratio_2 = angle_start_length/line_length_2
		angle_start_pos_1 = line_1_start_pt + ratio_1 * (line_1_end_pt - line_1_start_pt)
		angle_start_pos_2 = line_2_start_pt + ratio_2 * (line_2_end_pt - line_2_start_pt)

		# plt.plot([angle_start_pos_1[0],angle_start_pos_2[0]], [angle_start_pos_1[1],angle_start_pos_2[1]], 'bo')

		# draw connecting curve
		# print("A : {} O : {} B : {}".format(angle_start_pos_1, line_1_start_pt, angle_start_pos_2))
		if is_right_angle: Line.draw_right_angle(angle_start_pos_1, line_1_start_pt, angle_start_pos_2)
		else: Line.draw_angle(angle_start_pos_1, line_1_start_pt, angle_start_pos_2, ax = self.ax)


class Triangle(Shape):
	def __init__(self, pts, custom_point_names=None):
		super(Triangle, self).__init__(pts, custom_point_names)
		

class Square(Shape):
	def __init__(self, pts, custom_point_names=None):
		super(Square, self).__init__(pts, custom_point_names)
