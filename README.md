# Network Scanner Pro

## Descripción
Aplicación para escanear un rango de IP en la red local, mostrando:
- IP de cada equipo
- Nombre del host
- Estado (activo/inactivo)
- Tiempo de respuesta (latencia)

## Funcionalidades
- Ingreso de IP de inicio y fin
- Configuración de timeout
- Escaneo de red con ping y nslookup
- Barra de progreso en tiempo real
- Guardado de resultados en CSV
- Tabla ordenable de resultados
- Tutorial integrado

## Problemas conocidos
- Algunos hosts pueden aparecer como “Desconocido” si el nslookup falla
- Barra de progreso funciona mejor en redes con respuesta rápida
