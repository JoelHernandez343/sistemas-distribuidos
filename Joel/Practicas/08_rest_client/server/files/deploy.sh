#!/bin/bash

# Pretty output
# Author: Tasos Latsas
# Modified: Joel Hernández
function _spinner() {
    # $1 start/stop
    #
    # on start: $2 display message
    # on stop : $2 process exit status
    #           $3 spinner function pid (supplied from stop_spinner)

    local on_success="DONE"
    local on_fail="FAIL"
    local white="\e[1;37m"
    local green="\e[1;32m"
    local red="\e[1;31m"
    local nc="\e[0m"
    
    case $1 in
        start)
            printf "   ⣾  ${2}"
            # start spinner
            i=1
            sp='⣷⣯⣟⡿⢿⣻⣽⣾'
            delay=${SPINNER_DELAY:-0.05}

            while :
            do
                j=1
                while [ $j -le $((${#2} + 3)) ]
                do
                    ((j++))
                    printf "\b"
                done

                printf "${sp:i++%${#sp}:1}  "
                echo -ne ${2}
                sleep $delay
            done
            ;;
        stop)
            if [[ -z ${3} ]]; then
                echo "spinner is not running.."
                exit 1
            fi

            kill $3 > /dev/null 2>&1

            let column=$(tput cols)
            j=1
            while [ $j -le $column ]
            do
                ((j++))
                printf "\b"
            done

            # inform the user uppon success or failure
            echo -en "   "
            if [[ $2 -eq 0 ]]; then
                echo -en "${5}"
            else
                echo -en "❌"
            fi
            echo -e " ${4}"
            ;;
        *)
            echo "invalid argument, try {start/stop}"
            exit 1
            ;;
    esac
}

message=""

function start_spinner {
    # $1 : msg to display
    message=`echo ${1}`
    _spinner "start" "${1}" &
    # set global spinner pid
    _sp_pid=$!
    disown
}

function stop_spinner {
    # $1 : command exit status
    _spinner "stop" $1 $_sp_pid "$message" "${2}"
    unset _sp_pid
}

printf "\n"


start_spinner 'Updating system and installing utilities'
sudo apt -qq update 2>/dev/null > /dev/null
sudo apt -qq -o Dpkg::Use-Pty=0 install openjdk-8-jdk-headless mysql-server unzip -y -qq 2>/dev/null > /dev/null
stop_spinner $? '🚀'


start_spinner 'Installing tomcat'
wget -q https://downloads.apache.org/tomcat/tomcat-8/v8.5.60/bin/apache-tomcat-8.5.60.zip
unzip -qq apache*.zip
rm apache*.zip
cd apache*/
rm webapps -r
mkdir webapps
mkdir webapps/ROOT
wget -qq https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.24/jaxrs-ri-2.24.zip
unzip -qq jax*.zip
rm jax*.zip
cp jaxrs-ri/api/*.jar lib
cp jaxrs-ri/ext/*.jar lib
cp jaxrs-ri/lib/*.jar lib
rm jaxrs-ri/ -r
rm lib/javax.servlet-api-3.0.1.jar
cd lib
wget -q https://repo1.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar
wget -q https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.22.zip
unzip -qq mysql*.zip
cp mysql*/mysql*.jar .
rm mysql*/ -r
rm mysql*.zip
stop_spinner $? '🐈'


cd ../
export CATALINA_HOME=$(pwd)
cd ../
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

printf "   📦 Configuring MySQL:\n"
printf "      Please enter 'root' as root's password and 'hugo' as hugo password\n"
sudo mysql_secure_installation
sudo mysql < sudo.sql
printf "      Enter root's password: (root)\n"
mysql -u root -p < root.sql
printf "      Enter hugo's password: (hugo)\n"
mysql -u hugo -p < db.sql
printf "   📦 Configured MySQL\n"


start_spinner 'Deploying app'
unzip -qq Servicio.zip
rm Servicio.zip
cd Servicio
javac -cp $CATALINA_HOME/lib/javax.ws.rs-api-2.0.1.jar:$CATALINA_HOME/lib/gson-2.3.1.jar:. negocio/Servicio.java
rm -f WEB-INF/classes/negocio/*
cp negocio/*.class WEB-INF/classes/negocio/
jar cvf Servicio.war WEB-INF META-INF > /dev/null
rm -f $CATALINA_HOME/webapps/Servicio.war
rm -f -r $CATALINA_HOME/webapps/Servicio
cp Servicio.war $CATALINA_HOME/webapps
cd ../
mv usuario_sin_foto.png $CATALINA_HOME/webapps/ROOT/
sh $CATALINA_HOME/bin/catalina.sh start > /dev/null
stop_spinner $? '🎉'
printf "\n   Tomcat server is running at port 8080. Stop with ${CATALINA_HOME}/bin/catalina.sh stop\n"

