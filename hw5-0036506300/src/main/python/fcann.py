import numpy as np
from typing import List
import sys

def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def re_lu(x):
    return np.maximum(0, x)


def softmax(x):
    exp_x_shifted = np.exp(x - np.max(x))
    exp_sums = np.sum(exp_x_shifted, axis=1).reshape((-1, 1))
    probs = exp_x_shifted / exp_sums
    return probs


class SoftmaxLayer:
    def __init__(self):
        self.probs = None

    def forward(self, x):
        self.probs = softmax(x)
        return self.probs

    def backward(self, y_true):
        assert y_true.shape == self.probs.shape
        return self.probs - y_true

    def optimization_step(self, lr=0.01):
        return


class SigmoidLayer:
    def __init__(self):
        self.h = None

    def forward(self, s):
        self.h = sigmoid(s)
        return self.h

    def backward(self, dL_dh):
        assert dL_dh.shape == self.h.shape, \
            "gradient should bw the same shape as output of the sigmoid"
        return dL_dh * self.h * (1 - self.h)

    def optimization_step(self, lr=0.01):
        return


class LinearLayer:
    def __init__(self, dim_in, dim_out):
        self.input = None
        self.W = np.random.randn(dim_in, dim_out)
        self.b = np.random.rand(1, dim_out)
        self.dL_dW = None
        self.dL_db = None

    def forward(self, x):
        self.input = x
        s = x @ self.W + self.b
        return s

    def backward(self, dL_ds):
        self.dL_dW = self.input.T @ dL_ds
        self.dL_db = np.sum(dL_ds, axis=0).reshape((1, -1))
        assert self.dL_dW.shape == self.W.shape
        assert self.dL_db.shape == self.b.shape, f"{self.dL_db.shape} {self.b.shape}"
        # dL_d_input
        return dL_ds @ self.W.T

    def optimization_step(self, lr=0.01):
        self.W -= lr * self.dL_dW
        self.b -= lr * self.dL_db


class FCANN:
    """Fully connected artificial neural network."""

    def __init__(self, dimensions: List[int]):
        d = dimensions[0]
        c = dimensions[-1]
        self.probs = None
        self.layers = []
        previous = d
        for dim in dimensions[1:-1]:
            self.layers.append(LinearLayer(previous, dim))
            self.layers.append(SigmoidLayer())
            previous = dim
        self.layers.append(LinearLayer(previous, c))
        self.layers.append(SoftmaxLayer())

    def forward(self, x):
        h = x
        for layer in self.layers:
            h = layer.forward(h)

        return h

    def backward(self, y_true):
        # it is expected the last layer determines dL_dh with y_true
        dL_dh = y_true
        for layer in reversed(self.layers):
            dL_dh = layer.backward(dL_dh)

    def optimization_step(self, lr=0.01):
        for layer in self.layers:
            layer.optimization_step(lr)


def MSE(y_predicted, y_true):
    error = y_predicted - y_true
    sum_squared_error = np.sum(error * error, axis=1)
    return np.mean(sum_squared_error)


def train(model, dataset, loss_fun, epochs=10000, lr=0.0001, print_period=100, threshold=0.01):
    for i in range(epochs):
        for batch in dataset:
            x, y = batch
            model.forward(x)
            model.backward(y)
            model.optimization_step(lr)

        # calculate loss on entire dataset
        x, y = dataset.x, dataset.y
        probs = model.forward(x)
        loss = loss_fun(probs, y)
        # if threshold is satisfied, print and break
        if loss < threshold:
            print("[{:5}] loss = {}".format(i, loss))
            sys.stdout.flush()
            break
        # print every print_period epochs
        if i % print_period == 0:
            print("[{:5}] loss = {}".format(i, loss))
            sys.stdout.flush()


def shuffle_data(data_x, data_y):
    indices = np.arange(data_x.shape[0])
    np.random.shuffle(indices)
    shuffled_data_x = np.ascontiguousarray(data_x[indices])
    shuffled_data_y = np.ascontiguousarray(data_y[indices])
    return shuffled_data_x, shuffled_data_y


class Dataset:

    def __init__(self, x, y, batch_size=None, shuffle=True):
        assert len(x) == len(y)
        self.x = x
        self.y = y
        self.batch_size = len(x) if batch_size is None else batch_size
        self.shuffle = shuffle

    def __iter__(self):
        if self.shuffle is True:
            self.x, self.y = shuffle_data(self.x, self.y)
        index = 0
        while index < len(self.x):
            yield self.x[index:index + self.batch_size],\
                  self.y[index:index + self.batch_size]
            index += self.batch_size
