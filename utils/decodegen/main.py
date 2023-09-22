import csv
import re
import os

decodetablegen= ''
with open('docs/控制信号表.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    ncol = len(next(csv_reader))
    nrow = 0
    for row in csv_reader:
        if nrow != 0:
            decodetablegen += ','
        decodetablegen += f'\nInstList.{row[0]} -> List(\n'
        for i in range(1, ncol - 1):
            decodetablegen += f'ControlMacros.{row[i]},\n'
        decodetablegen += f'ControlMacros.{row[ncol - 1]}\n'
        decodetablegen += f')'
        nrow += 1
decodetablegen += '\n'
decodetablegen = 'Array(' + decodetablegen + ')'

src = ''
with open('playground/src/control/DecodeTable.scala', 'r') as file:
    src = file.read()

pattern = r'Array\((.*)\)'
dest = re.sub(pattern, decodetablegen, src, flags=re.MULTILINE | re.DOTALL)
with open('playground/src/control/DecodeTable.scala', 'w') as file:
    file.write(dest)

os.system("make reformat")
