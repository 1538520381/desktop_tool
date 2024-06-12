<template>
  <div id="home">
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
      <component :is="menuItem[menuItemActive].component" />
    </div>
  </div>
</template>

<script>
import Ledger from '@/views/ledger'
export default {
  name: "Home",
  data() {
    return {
      menuItem: [
        {
          name: "账本",
          component: Ledger,
        },
      ],
      menuItemActive: 0,
    };
  },
  methods: {
    selectMenuItem(index) {
      this.menuItemActive = index;
    },
  },
};
</script>

<style scoped>
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
</style>