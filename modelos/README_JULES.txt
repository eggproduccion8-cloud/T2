MUNDO DE TRONOS - ASSETS BASE PARA PROYECTO NUEVO
===================================================
Este ZIP contiene recursos visuales y datos de modelos/animaciones para reconstruir
el mod desde cero.

NO es el proyecto Forge y NO contiene la arquitectura Java del proyecto viejo.
No incluye misiones, diálogos, reinos, comandos antiguos ni lógica YAML de plugins.

CONTENIDO:
- NPC_Vol1: modelos .bbmodel, animaciones, texturas y recursos visuales.
- NPC_Vol2: modelos .bbmodel, animaciones, texturas y recursos visuales.
- Crates: modelos Blockbench, modelos JSON y texturas.
- Furniture: recursos de muebles (Crucible, ItemsAdder y Oraxen) como referencia visual.
- TrainingDummy: dummy.bbmodel.

REGLA PARA JULES:
Usar este ZIP como FUENTE DE ASSETS. No copiar la arquitectura de plugins originales.
No instalar ModelEngine/ItemsAdder/Oraxen/Crucible como requisito del mod.
El nuevo mod debe implementar su propio sistema de renderizado y animación.

PRIORIDAD:
1. Guard de Vol.1: modelo + textura.
2. idle.
3. walk + movimiento real.
4. animaciones temporales.
5. resto de NPCs.
6. Vol.2, crates, muebles y dummy.

Los YAML de los packs originales se excluyen intencionalmente porque son configuración/lógica
de plugins y no deben contaminar el proyecto nuevo.
