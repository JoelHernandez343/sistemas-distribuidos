#!/usr/bin/env python3
import sys, time

from paramiko import SSHClient, AutoAddPolicy
from scp import SCPClient

files = [
    'ServerRMI.java', 'InterfaceRMI.java', 'Makefile', 'ConsoleColors.java'
]

nodes = [
    {
        'ip': '13.68.181.122',
        'dest': '~/practice_06/',
        'user': 'joel',
        'password': 'Relampago.123',
        'files': files + ['ClientRMI.java']
    },
    {
        'ip': '13.82.215.91',
        'dest': '~/practice_06/',
        'user': 'joel',
        'password': 'Relampago.123',
        'files': files
    },
    {
        'ip': '13.92.225.140',
        'dest': '~/practice_06/',
        'user': 'joel',
        'password': 'Relampago.123',
        'files': files
    },
    {
        'ip': '13.82.45.250',
        'dest': '~/practice_06/',
        'user': 'joel',
        'password': 'Relampago.123',
        'files': files
    }
]

def check_args():
    if len(sys.argv) == 1:
        return files + ['ClientRMI.java']
    else:
        return sys.argv

files_to_send = check_args()

for i, node in enumerate(nodes, start=0):
    ssh = SSHClient()
    ssh.load_system_host_keys()
    ssh.set_missing_host_key_policy(AutoAddPolicy())
    ssh.connect(node['ip'], username=node['user'], password=node['password'])
    stdin_, stdout_, stderr_ = ssh.exec_command("mkdir " + node['dest'])

    stdout_.channel.recv_exit_status()
    lines = stdout_.readlines()
    for line in lines:
        print(line)

    scp = SCPClient(ssh.get_transport())

    for file in node['files']:
        if file in files_to_send:
            scp.put(file, remote_path=node['dest'])
            print('[nodo:', i, ', ip:', node['ip'], '] Enviado', file, 'a', node['dest'])

    scp.close()
    ssh.close()
    
