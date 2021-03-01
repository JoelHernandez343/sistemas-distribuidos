#!/bin/sh
# Created by Joel Hernández @ 2020

if [ $# -ne 3 ]; then
    printf "Usage: $0 <server-ip> <client1-ip> <client2-ip>\n"
    exit 1
fi

SERVER=$1
C1=$2
C2=$3
USER="joel"
PASS="EstoEsUnPassword123"

SERVER_DIR="/var/nfs/servidor"
CLIENT_DIR="/nfs/cliente"

printf "\n\n     📄 Showing texto.txt at Client 1 ${C1} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "cat ${CLIENT_DIR}/texto.txt"
printf "\n\n     📄 Showing texto.txt at Client 2 ${C2} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C2} "cat ${CLIENT_DIR}/texto.txt"
printf "\n\n     📄 Adding some text to texto.txt at Client 2 ${C2} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C2} "echo 'estamos agregando texto al archivo' >> ${CLIENT_DIR}/texto.txt"
printf "\n\n     📄 Showing texto.txt at Client 1 ${C1} again \n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "cat ${CLIENT_DIR}/texto.txt"
printf "\n\n     ❌ Deleting texto.txt at Client 1 ${C1}\n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "rm ${CLIENT_DIR}/texto.txt"
printf "\n\n     📎 Listing all files at Client 1 ${C1}\n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "ls -la ${CLIENT_DIR}"
printf "\n\n     📎 Listing all files at Client 2 ${C2}\n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "ls -la ${CLIENT_DIR}"