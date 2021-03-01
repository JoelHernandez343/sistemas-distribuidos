#!/bin/bash
make ServerRMI.class
trap 'kill %1; kill %2; kill %3;' SIGINT;
java ServerRMI $1 1099 & java ServerRMI $1  1100 & java ServerRMI $1  1101 & java ServerRMI $1 1102
trap - SIGINT