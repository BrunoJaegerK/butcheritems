# ButcherItems

Plugin de ClearLag para servidores Minecraft Bedrock que utilizan Nukkit. Elimina entidades para reducir la acumulación de entidades innecesarias y ayudar a mantener el rendimiento del servidor.

## Características

- Limpieza automática configurable por intervalo.
- Comando `/clearlag` para ejecutar una limpieza manual.
- Anuncios configurables por chat, título o actionbar.
- Mensajes personalizables mediante `messages.yml`.
- Protección de jugadores, pinturas, botes, vehículos y cristales del End.
- Compatibilidad con LlamaPets, PlaceholderAPI y NPC Slapper.
- Protección de hologramas de **PetteriM1 Holograms** y del plugin Holograms de Cloudburst.
- Exclusión configurable de entidades y mundos.
- Compatible con Java 17.

## Instalación

1. Descarga el JAR de la sección **Releases** o de Cloudburst.
2. Coloca `ButcherItems.jar` en la carpeta `plugins` del servidor.
3. Inicia el servidor para generar los archivos de configuración.
4. Ajusta `plugins/ButcherItems/config.yml` según tus necesidades.

## Configuración

### `config.yml`

```yaml
Schedule: 300
Announcement: title
KillEntitiesWithNametags: true

ExcludedEntities:
  - ZOMBIE
  - EntityCreeper

ExcludedWorlds:
  - lobby
  - survival_build

Announcements:
  - 60
  - 30
  - 15
  - 10
  - 5
  - 3
  - 2
  - 1

Commands:
  Clearlag:
    Name: "clearlag"
    Description: "Clear all the lag."
    Permission: "butcheritems.clearlag"
    Usage: "/clearlag"
```

- **Schedule**: intervalo de limpieza en segundos.
- **Announcement**: formato de anuncio: `chat`, `title` o `actionbar`.
- **KillEntitiesWithNametags**: si está activado, las entidades con nombre personalizado también pueden eliminarse. Desactívalo para protegerlas.
- **ExcludedEntities**: lista de entidades que nunca se eliminarán. Admite nombres como `ZOMBIE`, `EntityZombie` o el nombre completo de la clase.
- **ExcludedWorlds**: mundos que ClearLag ignorará por completo. La comparación no distingue mayúsculas y minúsculas.
- **Announcements**: segundos restantes en los que se enviará un aviso. Usa una lista vacía (`[]`) para desactivar los avisos previos.

### `messages.yml`

Permite personalizar el prefijo, los mensajes de limpieza, los títulos, la actionbar y las palabras en singular/plural. Conserva los marcadores `[0]` y `[1]`, ya que se sustituyen por cantidades y unidades.

## Holograms

ButcherItems detecta y protege las entidades del paquete `gt.creeperface.holograms.entity` utilizado por [PetteriM1/Holograms](https://github.com/PetteriM1/Holograms). También conserva la compatibilidad anterior con el paquete `me.onebone.holograms`.

La integración es opcional: no necesitas instalar Holograms para utilizar ButcherItems.

## Comandos y permisos

| Comando | Permiso | Descripción |
|---|---|---|
| `/clearlag` | `butcheritems.clearlag` | Ejecuta una limpieza manual. |

## Dependencias e integraciones

- Nukkit / NukkitPetteriM1Edition.
- PlaceholderAPI (opcional, para el placeholder `%clearlag%`).
- LlamaPets (opcional; sus entidades se protegen automáticamente).
- PetteriM1 Holograms (opcional; sus hologramas se protegen automáticamente).

## Compilación

Requiere JDK 17 y Maven:

```bash
mvn clean package
```

El JAR se genera en `target/butcheritems-1.1.0.jar`.

## Reportar errores

Si encuentras un error o tienes una sugerencia, abre un issue en [GitHub](https://github.com/BrunoJaegerK/butcheritems/issues).

