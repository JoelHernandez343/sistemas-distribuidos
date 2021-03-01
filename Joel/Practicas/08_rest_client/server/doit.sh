#!/bin/bash
chmod +x upload.sh
# Modifica los datos en credentials.txt
./upload.sh < credentials.txt

# En el servidor, cambias a la carpeta server
# cd server
# Y ejecutas deploy.sh
# ./deploy.sh
# Recuerda que la contraseña del root es root y de hugo es hugo.