# Interrapidísimo Test App - Fabio Andrés Sánchez Quintana

Este proyecto fue desarrollado como parte de la prueba técnica solicitada por Inter Rapidisimo. 
Es una aplicación funcional desarrollada en **Kotlin** para Android, siguiendo principios SOLID, con arquitectura limpia y consumo de servicios REST.

---

## ✅ Requisitos funcionales implementados:

1. **Control de versión del aplicativo**
   - Compara la versión local con la obtenida desde la API.
   - Muestra mensaje si la versión es inferior o superior.

2. **Login**
   - Consumo de API con headers personalizados.
   - Manejo de códigos de error HTTP.
   - Almacena en base de datos local: Usuario, Identificación y Nombre.

3. **Capa de Datos**
   - Uso de **Room (SQLite)**.
   - Se almacenan localmente las tablas obtenidas desde el endpoint `/ObtenerEsquema/true`.

4. **Capa de Presentación**
   - Pantalla **Home** con datos del usuario y botones de navegación.
   - Pantalla **Tablas**: muestra la lista de tablas guardadas localmente.
   - Pantalla **Localidades**: consume y muestra la lista directamente desde la API.

---

## ⚙️ Tecnologías utilizadas:

- Kotlin
- Android Jetpack (ViewModel, StateFlow, Navigation)
- Room (SQLite)
- Retrofit
- Hilt (inyección de dependencias)
- Material 3 / Jetpack Compose

---

## 📦 Instrucciones para abrir el proyecto:

1. Abrir **Android Studio**.
2. Seleccionar **"Open Project"** y buscar la carpeta del proyecto.
3. Esperar a que se sincronice Gradle.
4. Ejecutar en un emulador o dispositivo físico.

---

## 🛠️ Notas:

- Se implementó manejo de excepciones con `try-catch`.
- Todas las respuestas de red manejan errores apropiadamente.
- Código estructurado por capas (data, domain, ui) con comentarios claros.
- El proyecto no incluye archivos de construcción (`build/`, `.gradle/`) para reducir tamaño del zip.

---

Autor: Fabio Andrés Sánchez Quintana  
Email: [fabioandressq@hotmail.com]
