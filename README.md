# Tarea 1 Distrubuidos
## Integrantes: 
## - Giorgio Pellizzari Águila 
## - Gabriel Valenzuela Lorca

Para compilar los archivos, se debe situar en la carpeta "src" por consola y ejecutar el comando "make".

Si se desea crear un servidor central, se debe ingresar "make ServidorCentral".

Si se desea crear un servidor de Cliente, se debe ingresar "make Cliente".

Si se desea crear un servidor de Distrito, se debe ingresar "make Distrito".

Se puede usar "make clean" para limpiar los archivos creados por make.

Se pide que se cree primero un Servidor Central y se le ingresen los valores correspondientes. Posteriormente se deben crear los distritos y finalmente los clientes para no alterar el flujo de las cosas.

El distrito solicitará la dirección IP del Servidor Central para intercambiar datos valiosos. El Servidor Central tiene reservado el puerto 9000 por defecto, y no se puede hacer uso del puerto 9090 ya que se usa con otros fines.

La lista de titanes por distrito es actualizada periodicamente en cortos intervalos de tiempo.
