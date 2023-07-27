import csv

with open('docs/控制信号表.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    for row in csv_reader:
        if line_count != 0:
            print(f'InstList.{row[0]} -> List(')
            for i in range(1, 12):
                print(f'ControlMacro.{row[i]},')
            print(f'ControlMacro.{row[12]}')
            print(f'),')
        line_count += 1
