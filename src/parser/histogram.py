#-*- coding:utf-8 -*-

import seaborn as sns
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import numpy as np
import time
import sys
# import time
import os


def save_plot(*args):
	args = list(map(lambda x:x.replace("_"," "), args))
	rv_list = np.array(list(map(lambda x:float(x),args[4].split(","))))
	# print(rv_list)
	 # if len(sys.argv) > 1 else np.random.randn((20))*5+30
	rv_list = rv_list.astype(np.int)
	bin_edges = [i for i in range(rv_list.min() - rv_list.min()%5, rv_list.max()+6, 5)]
	virtual_bin_edges = [min(bin_edges)-5] + bin_edges[:] + [max(bin_edges) + 5]

	sns.set_style("whitegrid", {'grid.linestyle': '--'})

	# plot histogram
	ax = sns.distplot(rv_list,hist=True,kde=False,bins=bin_edges, color="#FF5733")
	ax.set_aspect(5)

	x_list = [(virtual_bin_edges[i+1]+virtual_bin_edges[i])*0.5 for i in range(len(virtual_bin_edges)-1)]
	y_list = [((rv_list >= virtual_bin_edges[i]) * (rv_list < virtual_bin_edges[i+1])).sum() for i in range(len(virtual_bin_edges)-1)]

	# line_data = pd.DataFrame({'x':x_list, 'y':y_list, 'style':['one' for _ in range(len(x_list))]})
	# ax_line = sns.lineplot(x='x', y='y', color='black', style='style', markers=True, data=line_data, legend=False)
	# ax_line.set_xlabel("")
	# ax_line.set_ylabel("")

	# set tick spaces manually
	ax.yaxis.set_major_locator(ticker.MultipleLocator(1))
	ax.xaxis.set_major_locator(ticker.MultipleLocator(5))
	

	# x,y axis tags
	font = {
	'family':'Malgun Gothic',
	'color':'black',
	'weight':'normal',
	'size':10
	}

	_, ylim = plt.ylim()
	xlim0, xlim = ax.get_xlim()

	plt.text(xlim0-(xlim-xlim0)*0.1, ylim, args[2],fontdict=font)
	plt.text(xlim, -0.5, args[3],fontdict=font)
	plt.title(args[1], fontdict=font)

	# set label coordinates
	filename = "{}\\..\\{}_{}.png".format(os.getcwd(),"histogram",time.strftime('%y%m%d%H%M%S', time.localtime(time.time())))
	plt.savefig(filename)
	return filename
	# print(filename)

def test():
	save_plot('','디스이즈 스파르타!','(a)','(b)', ",".join(list(map(lambda x:str(x), (np.random.randn((20))*5+30).tolist()))))


if __name__== "__main__":
	# test()
	print(save_plot(*sys.argv))

