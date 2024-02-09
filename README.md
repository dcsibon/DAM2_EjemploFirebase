## App Gestión de notas (EjemploFirebase)

Esta es una aplicación realizada en Android Studio con Kotlin, Jetpack Compose y Firebase.

El flujo de la aplicación se organiza alrededor de varias pantallas principales y utiliza Firebase para la autenticación de usuarios y el almacenamiento de datos de las notas. 
A continuación, se describe el flujo general de la aplicación desde el lanzamiento hasta las operaciones específicas de gestión de notas.

### Inicio de la Aplicación: MainActivity

1. **Lanzamiento de la Aplicación**: Cuando la aplicación se lanza, `MainActivity` se inicia y configura el entorno de la aplicación utilizando Jetpack Compose.

3. **Inicialización de ViewModels**: Dentro de `MainActivity`, se inicializan dos ViewModels importantes: `LoginViewModel` para la autenticación de usuarios y `NotesViewModel` para la gestión de notas.

4. **Establecimiento del Tema y la Navegación**: Se aplica el tema de la aplicación y se configura el sistema de navegación (`NavManager`), que controlará la transición entre las diferentes pantallas de la aplicación.

### Autenticación y Registro: BlankView, LoginView, RegisterView

1. **Pantalla Inicial (BlankView)**: La aplicación comienza con una pantalla inicial (`BlankView`) que decide automáticamente si el usuario debe ser redirigido a la pantalla de inicio (Home) si ya está autenticado o a la pantalla de inicio de sesión (Login) si no lo está.

2. **Inicio de Sesión y Registro (LoginView y RegisterView)**: Si el usuario necesita autenticarse, se le presenta una interfaz con pestañas que alternan entre el inicio de sesión y el registro, gestionadas por `TabsView`. Aquí, el usuario puede ingresar sus credenciales para iniciar sesión o crear una nueva cuenta. Esta interacción es gestionada por `LoginViewModel`.

### Gestión de Notas: HomeView, AddNoteView, EditNoteView

1. **Vista Principal (HomeView)**: Una vez autenticado, el usuario accede a la pantalla principal donde se listan sus notas. Esta pantalla permite al usuario navegar a la pantalla para añadir una nueva nota (`AddNoteView`) o seleccionar una nota existente para editarla (`EditNoteView`). La lista de notas se obtiene y gestiona mediante `NotesViewModel`.

2. **Añadir Nueva Nota (AddNoteView)**: En esta pantalla, el usuario puede escribir el título y el contenido de una nueva nota. Al guardar, la nota se añade a Firebase Firestore y el usuario regresa a la pantalla principal.

3. **Editar Nota Existente (EditNoteView)**: Aquí, el usuario puede modificar el título y el contenido de una nota existente. Los cambios se guardan en Firestore, y el usuario puede eliminar la nota si lo desea.

### Navegación

- La navegación entre pantallas se gestiona a través de `NavManager`, que utiliza un `NavController` para manejar las transiciones y pasar datos entre las pantallas, como el ID de una nota cuando se navega a la pantalla de edición.

### ViewModel y Estado

- **LoginViewModel**: Gestiona la lógica de autenticación y registro, manteniendo el estado de los campos de entrada y los mensajes de error.
- **NotesViewModel**: Mantiene el estado de las notas (lista de notas, nota actual para edición), gestiona la recuperación, adición, edición y eliminación de notas en Firestore.

### Comunicación con Firebase

- **Autenticación**: `LoginViewModel` utiliza Firebase Auth para registrar nuevos usuarios y autenticar usuarios existentes.
- **Almacenamiento de Datos**: `NotesViewModel` interactúa con Firebase Firestore para realizar operaciones CRUD sobre las notas del usuario.

### Flujo General

El flujo de la aplicación es intuitivo y está centrado en el usuario, permitiendo una navegación fluida entre la autenticación y la gestión de notas. 

La integración con Firebase asegura que los datos del usuario estén seguros y accesibles en cualquier dispositivo, proporcionando una experiencia de usuario coherente y confiable.
