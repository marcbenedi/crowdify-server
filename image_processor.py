import image_slicer
import time
import numpy
import requests
from StringIO import StringIO
from multiprocessing import Pool
from PIL import Image

def rgb_to_hex(r,g,b):
	hexchars = "0123456789ABCDEF"
	return "#" + hexchars[r / 16] + hexchars[r % 16] + hexchars[g / 16] + hexchars[g % 16] + hexchars[b / 16] + hexchars[b % 16]

#not being used
def get_main_color(file):
    img = Image.open(file)
    colors = img.getcolors(100000) #put a higher value if there are many colors in your image
    max_occurence, most_present = 0, 0
    try:
        for c in colors:
            if c[0] > max_occurence:
                (max_occurence, most_present) = c
        return rgb_to_hex(most_present[0],most_present[1],most_present[2])
    except TypeError:
        raise Exception("Too many colors in the image")

#not being used
def slice_image(path, slices):
    image_slicer.slice(path, slices)

#not being used
def reduce_number_of_colors(file):
    image = Image.open(file)
    image.convert('P', palette=Image.ADAPTIVE, colors=64)

#not being used
def get_matrix_colors(path, n, m):
    init_miliseconds = int(round(time.time() * 1000))
    #reduce_number_of_colors(path)
    slice_image(path, n*m)
    file_name, file_format = path.split('.')
    args = []
    for i in range(n):
        for j in range(m):
            opt_x = "_" if i+1 > 9 else "_0"
            opt_y = "_"if j+1 > 9 else "_0"
            args.append(file_name+opt_x+str(i+1)+opt_y+str(j+1)+"."+file_format)
    pool = Pool()
    try:
        results = pool.map(get_main_color, args)
    except TypeError:
        raise Exception("IO error")
    end_miliseconds = int(round(time.time() * 1000))
    print end_miliseconds-init_miliseconds
    return results

#resizes the image
def resize(path, new_width, new_height, image):
    try:
        img = image
        wpercent = (new_width/float(img.size[0]))
        file_name, file_format = path.split('.')
        new_name = file_name+'_resized_'+str(new_width)+"_"+str(new_height)+"."+file_format
        img = img.resize((new_width,new_height), Image.ANTIALIAS)
        img.save(new_name)
        return new_name
    except Exception as e:
        print e

#calls resize and get_image
def resize_and_get_pixels(url, new_width, new_height):
    try:
        response = requests.get(url)
        img = Image.open(StringIO(response.content))
        print "INTERNET IMAGE OPENED SUCCESFULLY"
        image_name = url.split('/')[-1]
        f = resize(image_name, new_width, new_height, img)
        return get_image(f)
    except Exception as e:
        print e

#returns de hex colors
def get_image(image_path):
    try:
        image = Image.open(image_path, 'r')
        width, height = image.size
        pixel_values = list(image.getdata())
        ls = []
        for i in range(width):
            for j in range(height):
                l = []
                x = image.getpixel((i,j))
                hex_values = rgb_to_hex(x[0],x[1],x[2])
                l.append(hex_values)
                l.append(i)
                l.append(j)
                ls.append(l)
        return ls
    except Exception as e:
        print e
