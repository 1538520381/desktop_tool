<template>
  <div id="home">
    <div class="set">
      <el-upload
        class="upload"
        :auto-upload="false"
        :on-change="importData"
        :show-file-list="false"
      >
        <svg-icon class="icon" icon-class="import" />
      </el-upload>
      <svg-icon class="icon" icon-class="export" @click="exportData" />
      <svg-icon class="icon" icon-class="setup" @click="openSetupDialog" />
    </div>
    <div class="menu">
      <el-button
        class="menu-item"
        :class="{ 'menu-item-hover': index === menuItemActive }"
        v-for="(item, index) in menuItem"
        :key="index"
        @click="selectMenuItem(index)"
        >{{ item.name }}</el-button
      >
    </div>
    <div class="main">
      <component :is="menuItem[menuItemActive].component" :key="refreshFlag" />
    </div>
    <el-dialog
      class="setupDialog"
      v-model="setupDialogVisible"
      width="80%"
      title="设置"
    >
      <el-form class="form" :model="setup">
        <el-form-item class="item" label="开机自启动" label-width="100px">
          <el-switch
            class="selfStartUp"
            v-model="setup.selfStartUp"
            @change="setSelfStartUp"
          />
        </el-form-item>
        <el-form-item class="item" label="百度AppId" label-width="100px">
          <el-input
            v-model="setup.baiduAppId"
            type="password"
            @change="setBaiduAppId"
          />
        </el-form-item>
        <el-form-item class="item" label="百度密钥" label-width="100px">
          <el-input
            v-model="setup.baiduSecretKey"
            type="password"
            @change="setBaiduSecretKey"
          />
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Todo from "@/views/todo";
import Ledger from "@/views/ledger";
import Translate from "@/views/translation";

import { read, write } from "@/utils/IOUtil";
import { openSelfStartUp, closeSelfStartUp } from "@/utils/selfStartUpUtil";
import { compress, decompress } from "@/utils/compressUtil";

import { ElMessageBox } from "element-plus";
import { ipcRenderer } from "electron";

const path =
  process.env.NODE_ENV === "production"
    ? localStorage.getItem("appPath")
    : process.cwd();
export default {
  name: "Home",
  data() {
    return {
      setup: null,

      menuItem: [
        {
          name: "待做",
          component: Todo,
        },
        {
          name: "账本",
          component: Ledger,
        },
        {
          name: "翻译",
          component: Translate,
        },
      ],

      menuItemActive: 0,

      setupDialogVisible: false,

      refreshFlag: false,
    };
  },
  created() {
    this.getSetup();
  },
  methods: {
    // 修改设置
    updateSetup() {
      return write("/src/json/setup.json", this.setup);
    },

    // 获取设置
    getSetup() {
      read("/src/json/setup.json")
        .then((res) => {
          this.setup = res.data;
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 打开设置对话框
    openSetupDialog() {
      this.setupDialogVisible = true;
    },

    // 选择菜单
    selectMenuItem(index) {
      this.menuItemActive = index;
    },

    // 设置开机自启动
    setSelfStartUp() {
      this.updateSetup()
        .then((res) => {
          if (this.setup.selfStartUp) {
            openSelfStartUp();
          } else {
            closeSelfStartUp();
          }
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 设置百度AppId
    setBaiduAppId() {
      this.updateSetup()
        .then((res) => {})
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 设置百度密钥
    setBaiduSecretKey() {
      this.updateSetup()
        .then((res) => {})
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 导入数据
    importData(file) {
      ElMessageBox.confirm("这将会覆盖现有数据，确定导入?", "Warning", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let res = decompress(file.raw.path, path + "/src/data");
          if (res.code === 0) {
            this.refreshFlag = !this.refreshFlag;
            this.$message.success("导入成功");
          } else {
            this.$message.error(res.err.toString());
            console.log(res.err);
          }
        })
        .catch(() => {});
    },
    // 导出数据
    exportData() {
      ipcRenderer.invoke("selectDirectory").then((res1) => {
        if (res1.code === 0) {
          let res2 = compress(path + "/src/data", res1.path + "\\data.zip");
          if (res2.code === 0) {
            this.$message.success("导出成功");
          } else {
            this.$message.error(res2.err.toString());
            console.log(res2.err);
          }
        } else {
          this.$message.error(res1.err.toString());
          console.log(res1.err);
        }
      });
    },
  },
};
</script>

<style scoped>
#home .set {
  text-align: right;
}

#home .set .upload {
  display: inline-block;
}

#home .set .icon {
  width: 20px;
  height: 20px;

  cursor: pointer;
}

#home .menu {
  padding: 10px 10px 10px 10px;
  border-radius: 10px;
  background-color: rgb(232, 230, 230);
}

#home .menu .menu-item {
  height: 25px;
  margin: 0px 0px 0px 2px;

  font-size: 12px;
}

#home .menu .menu-item-hover {
  border-color: #c6e2ff;
  background-color: #ecf5ff;

  color: #409eff;
}

#home .main {
  margin: 10px 0px 0px 0px;
  padding: 10px 10px 10px 10px;
  border-radius: 10px;
  background-color: rgb(232, 230, 230);
}

#home .setupDialog .form .item .selfStartUp {
  --el-switch-on-color: #13ce66;
  --el-switch-off-color: #ff4949;
}
</style>