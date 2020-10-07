# Tarea 1 | Cálculo distribuido de PI
## Introducción
En esta tarea se va a desarrollar un programa distribuido que calculará una aproximación de PI utilizando la serie de `Gregory-Leibniz`.

![serie](docs/00.png)

### Demostración de la serie Gregory-Leibniz:
Comenzando con la serie de Taylor:

![Serie de Taylor](docs/01.png)

Aplicamos la sustitución ![Sustitucion](docs/02.png) para obtener:

![Primera sustitucion](docs/03.png)

Ahora, ya que ![derivada](docs/04.png), integramos, encontramos que la expansión de Taylor para ![invtan](docs/05.png) es:

![invtan serie](docs/06.png)

Finalmente, encontramos la formula sustituyendo ![final](docs/07.png)

## Arquitectura del programa
El programa se va a ejecutar sobre cuatro nodos, cada nodo será una computadora diferente. Cada nodo (incluso el nodo 0) deberá calcular 10 millones de términos de la serie. Implementaremos la siguiente topología lógica de tipo estrella, cada nodo se ha identificado con un número entero:

![Star](docs/star.png)

El nodo 0 actuará como servidor y los nodos 1, 2 y 3 actuarán como clientes.

Dado que el requerimiento es desarrollar un solo programa, será necesario pasar como parámetro al programa el número de nodo actual, de manera que el programa pueda actual como servidor o como cliente, según el número de nodo que pasa como parámetro.

## Capturas

![running](docs/screenshot.png)