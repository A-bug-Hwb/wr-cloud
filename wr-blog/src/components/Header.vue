<script setup>
import {reactive} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()
let timer;

const data = reactive({
  routerUrl: router.currentRoute.value.fullPath,
  titleListShow: false,
})

function titleShow() {
  this.data.titleListShow = !this.data.titleListShow;
  if (this.data.titleListShow) {
    timer = setInterval(() => {
      this.data.titleListShow = false;
      clearInterval(timer); //清除定时器
      timer = 0;
      //设置定时器
    }, 3000);
  } else {
    clearInterval(timer); //清除定时器
    timer = 0;
  }
}

</script>

<template>
  <div class="header-seize"></div>
  <div class="header">
    <img class="header-list-img" src="../assets/image/header-list.png" alt=""
         @click="titleShow()">
    <ul class="title header-title">
      <router-link to="/index">
        <li class="header-li" :class="data.routerUrl === '/index'?'header-li-on':'header-li-out'">首页</li>
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
      <router-link to="/mini-games">
        <li class="header-li" :class="data.routerUrl === '/mini-games'?'header-li-on':'header-li-out'">小游戏</li>
      </router-link>
    </ul>
    <ul class="title header-title-list" v-show="data.titleListShow">
      <router-link to="/index">
        <li class="header-li" :class="data.routerUrl === '/index'?'header-li-on':'header-li-out'">首页</li>
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
      <router-link to="/mini-games">
        <li class="header-li" :class="data.routerUrl === '/mini-games'?'header-li-on':'header-li-out'">小游戏</li>
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
  color: #333333;
}

a:hover {
  color: red;
}

@media screen and (min-width: 1024px) {
  .header-seize {
    height: calc(88px - 2rem)
  }

  .header {
    height: 88px;

    .header-list-img {
      display: none;
    }

    .header-title {
      height: 100%;
      display: flex;
    }

    .header-title-list {
      display: none;
    }
  }


}

@media screen and (max-width: 1024px) {
  .header-seize {
    height: calc(50px - 2rem)
  }

  .header {
    height: 50px;

    .header-list-img {
      width: 30px;
      height: 30px;
      position: absolute;
      right: 2rem;
      top: 10px;
      opacity: 0.7;
    }

    .header-title {
      display: none;
    }

    .header-title-list {
      position: absolute;
      right: 0;
      top: 50px;
      flex-flow: column;
      height: 210px;
      background-color: RGB(255, 255, 255, 0.85);
    }
  }
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  background-color: RGB(255, 255, 255, 0.85);
  z-index: 99;

  .title {
    list-style: none;
    max-width: 1280px;
    margin: 0 auto;
    justify-content: space-evenly;
    align-items: center;
    padding: 0;

    .header-li {
      width: 120px;
      height: 40px;
      text-align: center;
      line-height: 40px;
    }

    .header-li-on::after {
      content: "";
      height: 2px;
      width: 40%;
      margin: 0 auto;
      background: red;
      display: block;
    }

    .header-li-out::after {
      content: "";
      height: 2px;
      width: 40%;
      margin: 0 auto;
      background: red;
      display: block;
      transform: scaleX(0);
      transition: all 0.3s;
    }

    .header-li-out:hover::after {
      transform: scaleX(1)
    }
  }
}

</style>