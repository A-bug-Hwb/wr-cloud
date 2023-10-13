<script setup>
import {reactive} from "vue";
import {useRouter} from "vue-router";
const router = useRouter()

const data = reactive({
  routerUrl:router.currentRoute.value.fullPath,
})


</script>

<template>
  <div class="header">
    <ul class="header-title">
      <router-link to="/">
        <li class="header-li" :class="data.routerUrl === '/'?'header-li-on':'header-li-out'">首页</li>
      </router-link>
      <router-link to="/introduce">
        <li class="header-li" :class="data.routerUrl === '/introduce'?'header-li-on':'header-li-out'">个人简介</li>
      </router-link>
      <router-link to="/experience">
        <li class="header-li" :class="data.routerUrl === '/experience'?'header-li-on':'header-li-out'">历程</li>
      </router-link>
      <router-link to="/resume">
        <li class="header-li" :class="data.routerUrl === '/resume'?'header-li-on':'header-li-out'">在线简历</li>
      </router-link>
    </ul>
  </div>
</template>

<style scoped>
.router-link-active {
  text-decoration: none;
  color: red;
}

a {
  color: white;
}

a:hover {
  background-color: RGB(255, 255, 255, 0);
  color: red;
}

.header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100px;
  background-color: RGB(0, 0, 0, 0.6);

  .header-title {
    list-style: none;
    max-width: 1280px;
    height: 100%;
    margin: 0 auto;
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    padding: 0;
    .header-li {
      width: 150px;
      height: 60px;
      text-align: center;
      line-height: 60px;
    }

    .header-li-on::after{
      content: "";
      height: 2px;
      width: 50%;
      margin: 0 auto;
      background: white;
      display: block;
    }
    .header-li-out::after {
      content: "";
      height: 2px;
      width: 50%;
      margin: 0 auto;
      background: white;
      display: block;
      transform: scaleX(0);
      transition:all 0.3s;
    }
    .header-li-out:hover::after{
      transform: scaleX(1)
    }
  }
}
</style>