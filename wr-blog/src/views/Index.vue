<script setup>
import Header from "@/components/Header.vue";
</script>

<template>
  <Header></Header>
  <div class="content">
    <div class="index-title">
      <div class="index-greet">
        <p class="greet-hi">Hi，</p>
        <p class="greet-word">欢迎访问我的博客</p>
      </div>
      <div class="index-github">
        <img style="width: 20px;height: 20px" src="../assets/image/github.png" alt="github.com"/>
        <a href="https://github.com/A-bug-Hwb" target="_blank" style="border-bottom: RGB(60, 148, 217,0.7) solid 1px">&nbsp;https://github.com/A-bug-Hwb</a>
      </div>
    </div>
    <el-row :gutter="20" class="content-row">
      <el-col :span="6" class="content-info">
        <img style="width: 180px;height: 180px;border-radius: 500px" src="../assets/image/header-img.png" alt=""/>
        <p class="name">郝文彬</p>
        <div class="content-info-icon">
          <img style="width: 18px;height: 18px" src="../assets/image/wechat.png" alt=""/>
          <img style="width: 18px;height: 18px" src="../assets/image/phone.png" alt=""/>
          <p>18736875376</p>
        </div>
        <div class="content-info-icon">
          <img style="width: 18px;height: 18px" src="../assets/image/email.png" alt=""/>
          <p>18736875376@163.com</p>
        </div>

        <div>

        </div>
      </el-col>
      <el-col :span="18" class="content-list content-list-min">
        <div class="list-item" v-for="i in 10">
          <img class="item-zd" src="../assets/image/zhiding.png" alt="" v-if="i<3">
          <p>title</p>
          <p>摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要</p>
        </div>
      </el-col>
      <el-col :span="24" class="content-list content-list-max">
        <div class="list-item" v-for="i in 10">
          <img class="item-zd" src="../assets/image/zhiding.png" alt="" v-if="i<3">
          <p>title</p>
          <p>摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要</p>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>

@media screen and (min-width: 1024px) {
  .content-info{
    display: flex;
  }
  .content-list-max{
    display: none;
  }
}

@media screen and (max-width: 1024px) {
  .content-info{
    display: none;
  }
  .content-list-min{
    display: none;
  }
}

.content {
  width: 86%;
  min-height: 100%;
  margin: 0 auto;
  background-color: RGB(255, 255, 255, 0.9);
  padding: 20px;

  .index-title {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    border-bottom: RGB(239, 242, 248) solid 1px;

    .index-greet {
      width: 100%;
      height: 50px;
      display: flex;
      justify-content: flex-start;
      align-items: flex-end;

      .greet-hi {
        font-size: 48px;
        font-weight: 800;
        font-family: "XingShu";
      }

      .greet-word {
        font-size: 26px;
        font-family: "Xingkai";
        padding-bottom: 10px;
      }
    }

    .index-github {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: 400px;
      margin-bottom: 15px;
    }
  }

  .content-row {
    margin-top: 20px;

    .content-info {
      height: 100%;
      flex-flow: column;
      justify-content: center;
      align-items: center;
      border-right: RGB(239, 242, 248) solid 1px;
      .name{
        font-size: 26px;
        font-family: "Xingkai";
        padding-bottom: 10px;
      }
      .content-info-icon{
        width: 80%;
        font-size: 14px;
        display: flex;
        justify-content: flex-start;
        align-items: center;
      }
    }

    .content-list {

      .list-item{
        position: relative;
        border-bottom: RGB(239, 242, 248) solid 1px;
        height: 150px;
        padding: 15px 10px;
        box-sizing: content-box;

        .item-zd{
          width: 32px;
          height: 32px;
          position: absolute;
          top: 0;
          right: 0;
          -moz-transform:rotate(90deg);
          -webkit-transform:rotate(90deg);
          filter:progid:DXImageTransform.Microsoft.BasicImage(rotation=3);
        }
      }
    }
  }

}
</style>