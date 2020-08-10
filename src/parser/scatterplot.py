#-*- coding:utf-8 -*-

import seaborn as sns
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import numpy as np
import pandas as pd 
import sys
import time
import os


def save_plot(*args):
	rv_x = np.array(list(map(lambda x:float(x),args[4].split(',')))) #np.random.randn((100))*5+165
	rv_y = np.array(list(map(lambda x:float(x),args[5].split(',')))) #np.array([item-100+np.random.randn((1))[0] for item in rv_x])

	data = pd.DataFrame({'x':rv_x,'y':rv_y})


	sns.set_style("whitegrid", {'grid.linestyle': '--'})

	# plot histogram
	ax = sns.scatterplot(x ='x', y= 'y', data = data, color='black')
	ax.set_aspect(5)
	ax.set_xlabel("")
	ax.set_ylabel("")

	# set tick spaces manually
	ax.yaxis.set_major_locator(ticker.MultipleLocator(5))
	ax.xaxis.set_major_locator(ticker.MultipleLocator(5))
	

	# x,y axis tags
	font = {
	'family':'Malgun Gothic',
	'color':'black',
	'weight':'normal',
	'size':10
	}

	ylim0, ylim = plt.ylim()
	xlim0, xlim = plt.xlim()
	ax.set_aspect(1)

	plt.text(xlim0-(xlim-xlim0)*0.1, ylim, args[3],fontdict=font)
	plt.text(xlim+1, ylim0-1, args[2],fontdict=font)
	plt.title(args[1], fontdict=font)

	# set label coordinates
	filename = "{}\\..\\{}_{}.png".format(os.getcwd(),"scatterplot",time.strftime('%y%m%d%H%M%S', time.localtime(time.time())))
	plt.savefig(filename)
	return filename
	# print(filename)

def test():
	save_plot('','디스이즈 스파르타!','(a)','(b)', ",".join(list(map(lambda x:str(x), (np.random.randn((20))*5+30).tolist()))), ",".join(list(map(lambda x:str(x), (np.random.randn((20))*5+30).tolist()))), 'histo.png')


if __name__== "__main__":
	# test()
	print(save_plot(*sys.argv))
