# 📍 Navegación por Madrid: Dijkstra vs. A*

https://github.com/marcos-almeida22/Navegacion-por-Madrid.git

Este proyecto es un motor de búsqueda de rutas optimizado para la ciudad de Madrid. Utiliza una red de más de 31,000 nodos e intersecciones extraídas de OpenStreetMap para calcular el trayecto más rápido entre dos puntos de la capital, comparando la eficiencia de algoritmos de búsqueda informada y no informada.

## 🚀 Características principales
* **Procesamiento de Big Data Geográfico**: Carga y gestión de grafos a partir de archivos CSV masivos generados mediante Python (OSMnx).

* **Modelo de Coste Real**: El "peso" de las aristas no es la distancia, sino el **tiempo de viaje**, calculado mediante la relación entre la longitud de la vía y su velocidad máxima permitida.

* **Algoritmos de Vanguardia**:

  * **Dijkstra**: Búsqueda exhaustiva para garantizar el camino mínimo.

  * A (A-Estrella):* Optimización mediante heurística admisible para reducir drásticamente el tiempo de cómputo.

* **Buscador Inteligente de Calles**: Índice inverso para localizar nodos mediante nombres de calles reales, gestionando normalización de texto y búsqueda de coincidencias.

## 🏗️ Estructura del Proyecto
El código sigue una arquitectura de capas para asegurar la escalabilidad:<code>
src/com/proyecto/
├── algoritmos/   # Implementación de Dijkstra, A* y gestión de estados (PriorityQueue)
├── model/        # Definición de objetos POJO (Nodo, Arista)
├── nucleo/       # Motor del grafo, lógica de distancias y cargador de datos CSV
└── ui/           # Interfaz de usuario por consola y punto de entrada (Main)</code>

## 🧮 Fundamentos Matemáticos
### Cálculo de Distancia Real
Para la heurística de A*, calculamos la distancia euclidiana aproximada utilizando las constantes de proyección para Madrid:

<img width="535" height="46" alt="image" src="https://github.com/user-attachments/assets/a70d3722-8c01-4a25-a4b8-50c3c9ce759a" />

### Heurística Admisible
Para garantizar la optimización de A* sin perder la ruta mínima, el algoritmo utiliza una función de estimación basada en la velocidad máxima del sistema (120 km/h):

<img width="337" height="65" alt="image" src="https://github.com/user-attachments/assets/545b5dff-3c4d-43b8-9078-26fbf28c076b"/>

## 🛠️ Instalación y Uso
1. **Clona el repositorio**:
<code>git clone https://github.com/marcos-almeida22/navegacion-madrid.git</code>
2. **Datos**: Asegúrate de que los archivos nodes.csv y edges.csv estén en la carpeta /csv en la raíz del proyecto.
3. **Compilación y Ejecución**:<code>
javac -d bin src/com/proyecto/**/*.java
java -cp bin com.proyecto.ui.Main</code>

## 🛠️ Tecnologías Utilizadas
* **Lenguaje**: Java 21
* **Extracción de Datos**: Python (OSMnx & Pandas)
* **Fuente de Datos**: OpenStreetMap (OSM)

## 🛠️ Próximas Mejoras
Para llevar este proyecto al siguiente nivel, se han identificado las siguientes líneas de evolución:
* **Interfaz Gráfica (GUI)**: Implementación de una interfaz visual utilizando JavaFX o Swing para visualizar el grafo de Madrid de forma interactiva sobre un mapa real.
* **Integración de Tráfico en Tiempo Real**: Modificar el cálculo del peso de las aristas ($travelTime$) mediante la integración de APIs externas para ajustar las velocidades según el estado del tráfico actual.
* **Navegación Multimodal**: Expandir el grafo para incluir la red de Metro de Madrid y Cercanías, permitiendo rutas que combinen tramos a pie y transporte público.
* **Optimización de Búsqueda (Contraction Hierarchies)**: Implementar técnicas de preprocesamiento de grafos para reducir el tiempo de respuesta en consultas de larga distancia a menos de 1ms.
* **Exportación de Rutas**: Capacidad de exportar el itinerario calculado en formatos estándar como GeoJSON o KML para su visualización en Google Earth o herramientas GIS.
* **Zonas de Bajas Emisiones (ZBE)**: Añadir filtros de búsqueda que eviten calles con restricciones de circulación según la etiqueta ambiental del vehículo.
