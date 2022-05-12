<template>
  <el-card>
    <h1 class="me-author-name">小贺个人空间</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;黑龙江&哈尔滨</span>
      <span><i class="me-icon-job"></i> &nbsp;java技术专家</span>
    </div>
    <div class="me-author-tool">
      <i @click="showTool(qq)" :title="qq.title" class="iconfont icon-QQ-circle-fill"></i>
      <i @click="showTool(github)" :title="github.title" class="iconfont icon-github"></i>
    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {
        qq: {title: 'QQ', message: '1911256412'},
        github: {
          title: 'github',
          message: '<a target="_blank" href="https://github.com/1911256412">https://github.com/1911256412</a>'
        }
      }
    },
    methods: {
      showTool(tool) {
        this.$message({
          duration: 0,
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: '<strong>' + tool.message + '</strong>'
        });
      }
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
</style>
