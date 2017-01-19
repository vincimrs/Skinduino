import sys
import time
from skinduino import Skinduino

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write('usage: {0} com_port'.format(sys.argv[0]))
        sys.exit(1)

    print('Connecting to Skinduino board...')
    sk = Skinduino()
    sk.connect(sys.argv[1])

    print('Setting capacitive baseline...')
    time.sleep(1)
    sk.setCapacitiveTouchBaseline()
    time.sleep(1)

    print('Printing capacitive touch values...')

    while True:
        try:
            values = sk.readCapacitiveTouchValues() # an array of integers in [0, 255]
            print(values)
        except (KeyboardInterrupt, SystemExit):
            pass