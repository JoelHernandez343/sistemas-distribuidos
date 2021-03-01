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

SERVER_COMMANDS="\
sudo apt update && \
sudo apt install nfs-kernel-server -y && \
sudo mkdir ${SERVER_DIR} -p && \
sudo chown nobody:nogroup ${SERVER_DIR} && \
sudo chmod 777 ${SERVER_DIR} && \
sudo echo -e \"${SERVER_DIR} ${C1}(rw,sync,no_subtree_check)\" | sudo tee -a /etc/exports > /dev/null && \
sudo echo -e \"${SERVER_DIR} ${C2}(rw,sync,no_subtree_check)\" | sudo tee -a /etc/exports > /dev/null && \
sudo exportfs -ra && \
sudo systemctl restart nfs-kernel-server && \
sudo systemctl enable nfs-kernel-server --quiet
"

CLIENT_COMMANDS="\
sudo apt update && \
sudo apt install nfs-common -y && \
sudo mkdir -p ${CLIENT_DIR} && \
sudo mount -v -t nfs ${SERVER}:${SERVER_DIR} ${CLIENT_DIR}
"

CLIENT_BOOT_CONFIG="\
echo -e \"${SERVER}:${SERVER_DIR}\t${CLIENT_DIR}\tnfs\tdefaults\t0\t0\" | sudo tee -a /etc/fstab &&
sudo reboot
"

printf "\n\n     🚀 Configuring Server \n\n"
sshpass -p ${PASS} ssh ${USER}@${SERVER} "${SERVER_COMMANDS}"
printf "\n\n     📦 Configuring Client 1 ${C1} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "${CLIENT_COMMANDS}"
printf "\n\n     📦 Configuring Client 2 ${C2} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C2} "${CLIENT_COMMANDS}"
printf "\n\n     📄 Creating texto.txt in Client 1 ${C1} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "echo 'esta es una prueba de NFS' > ${CLIENT_DIR}/texto.txt"
printf "\n\n     📄 Cheking texto.txt in Client 2 ${C2} \n\n"
sshpass -p ${PASS} ssh ${USER}@${C2} "cat ${CLIENT_DIR}/texto.txt"
printf "\n\n     📄 Configuring automatic mount in clients ans rebooting \n\n"
sshpass -p ${PASS} ssh ${USER}@${C1} "${CLIENT_BOOT_CONFIG}"
sshpass -p ${PASS} ssh ${USER}@${C2} "${CLIENT_BOOT_CONFIG}"
