import matplotlib.pyplot as plt
import numpy as np 
import os
from ShapeEngine import *

### test features

def get_random_pts():
	return np.random.randint(-5,5, size=(3,2)).astype(np.float)

def get_custom_pts():
	# note : order of points should be CLOCKWISE to make sure angles are displayed properly.
	return np.array([[-1,0],[-1,1], [2,2], [1,0]],dtype=np.float)


def test():
	set_rc()
	# random_points = np.concatenate((get_custom_pts(),np.array([[17,0]])+get_custom_pts()), axis=0)
	random_points = get_custom_pts()

	# set canvas
	scale = (random_points.max(0) - random_points.min(0)) * 0.15
	plt.xlim(random_points.min(0)[0] - scale[0], random_points.max(0)[0] + scale[0])
	plt.ylim(random_points.min(0)[1] - scale[1], random_points.max(0)[1] + scale[1])
	ax = plt.axes(label='main'); ax.set_aspect(1) # set aspect ratio

	sample_square_1 = Square(random_points.tolist())
	sample_square_1.draw()
	sample_square_1.set_line_color(0, color= "#FF3356")
	sample_square_1.set_line_color(2, color = "#FF3356")

	sample_square_1.mark_angle(0)
	sample_square_1.mark_line_length(0, r"$c$")
	sample_square_1.mark_line_length(2, r"$a$")

	sample_square_1.mark_length_aid_line(0)
	sample_square_1.mark_length_aid_line(2)


	plt.axis('off')
	plt.savefig('square.png', transparent=True)

def main(*args):
	# print(args)
	set_rc()
	random_points = map(lambda x:[float(item) for item in x.split(',')], args[1].split('~'))
	random_points = np.array(list(random_points))

	# set canvas
	scale = (random_points.max(0) - random_points.min(0)) * 0.15
	plt.xlim(random_points.min(0)[0] - scale[0], random_points.max(0)[0] + scale[0])
	plt.ylim(random_points.min(0)[1] - scale[1], random_points.max(0)[1] + scale[1])
	ax = plt.axes(label='main'); ax.set_aspect(1) # set aspect ratio

	sample_square_1 = Square(random_points.tolist())
	sample_square_1.draw(ax = ax)

	for item in args[2].split(','):
		idx,txt = item.split(':')
		sample_square_1.mark_line_length(int(idx), txt)
		sample_square_1.mark_length_aid_line(int(idx))
	
	for idx in args[3].split(','):
		sample_square_1.set_line_color(int(idx), color= "#FF3356")

	for idx in args[4].split(','):
		is_right_angle = False
		if ':r' in idx: 
			idx = idx.split(':')[0]
			is_right_angle = True
		sample_square_1.mark_angle(int(idx), is_right_angle= is_right_angle)

	plt.axis('off')

	# set label coordinates
	filename = "{}\\..\\{}_{}.png".format(os.getcwd(),"square",time.strftime('%y%m%d%H%M%S', time.localtime(time.time())))
	plt.savefig(filename, transparent=True)
	return filename


if __name__ == '__main__':
	# TEST
	# path = main(*r"/-1,0~-1,1~1,1~1,0/0:$\sqrt{2}$,2:?/0,2/0,2".split('/'))
	# print(path)

	# PRODUCTION
	print(main(*sys.argv))
