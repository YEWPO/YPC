import csv

with open('docs/控制信号表.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    ncol = len(next(csv_reader))
    for row in csv_reader:
        print(f'InstList.{row[0]} -> List(')
        for i in range(1, ncol - 1):
            print(f'ControlMacro.{row[i]},')
        print(f'ControlMacro.{row[ncol - 1]}')
        print(f'),')
