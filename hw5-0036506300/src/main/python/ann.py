import numpy as np
import pandas as pd
import fcann
import sys


if len(sys.argv) < 6:
    print("usage:"
          " python ann.py path_to_dataset architecture algorithm"
          " batch_size (-1 if algorithm is 'batch')"
          " epochs lr",
          file=sys.stderr)
    exit(-1)

path_to_dataset = sys.argv[1]
# expected "d,h_1,...,h_i,...,h_n,c"
architecture = [int(x) for x in sys.argv[2].split(",")]
batch_size = int(sys.argv[3])
if batch_size == -1:
    batch_size = None
epochs = int(sys.argv[4])
lr = float(sys.argv[5])

d = architecture[0]
c = architecture[-1]
# prepare training data
df = pd.read_csv(path_to_dataset, header=None)
x = np.array(df.iloc[:, :d])
y = np.array(df.iloc[:, d:])
dataset = fcann.Dataset(x, y, batch_size=batch_size)
# print(y)
# print(y.shape)

# print(architecture)
ann = fcann.FCANN(architecture)
fcann.train(ann, dataset, fcann.MSE, epochs, lr)
print("DONE")

while True:
    break
    sample = input()
    if sample == "STOP":
        break

    sample = np.array(sample.split(",")).astype(np.float64)
    print(ann.forward(sample))
