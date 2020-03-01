import numpy as np
import random
import math

n_range = [100,500,1000,2000,5000,10000,20000,100000,250000,500000,1000000]

for n in n_range:
    start = 0
    end = int(math.sqrt(n))

    X = np.random.uniform(start,end,(n, 2))
    # Y = np.random.uniform(start,end,1000)
    with open("uniform_fill/"+str(n), "w") as of:
        for x in X:
            of.write(str(x[0])+","+str(x[1])+"\n")
