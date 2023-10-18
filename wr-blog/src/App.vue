<script setup>
import {RouterView} from 'vue-router'
</script>

<template>
  <div class="body-background"></div>
  <RouterView/>
</template>

<style scoped>
.body-background {
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background-image: url("assets/image/background.jpg");
  background-size: 100% 100%;
  opacity: 0.4;
  z-index: -1;
}
</style>
