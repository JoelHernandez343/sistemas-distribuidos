#!/bin/bash
make ServerRMI.class
trap 'kill %1; kill %2; kill %3;' SIGINT;
rmiregistry 1099 & rmiregistry 1100 & rmiregistry 1101 & rmiregistry 1102
trap - SIGINT