<script setup>
import Header from "@/components/Header.vue";
</script>

<template>
  <Header></Header>
  <iframe class="game-life" src="../../src/assets/game-of-life.html">
  </iframe>
</template>

<style scoped>
a {
  color: royalblue;
}

a:hover {
  background-color: RGB(0, 0, 0, 0);
}

.game-life {
  width: 100%;
  height: 99%;
}
</style>