<template>
  <div id="ledger">
    <div class="overview">
      <el-button @click="readDetails"></el-button>
    </div>
    <div class="details"></div>
    <div class="control"></div>
  </div>
</template>

<script>
import { deleteByIds, insertOne, updateById, updateByIds } from "@/dao/index";
export default {
  name: "Ledger",
  data() {
    return {
      details: [],
      account: [],
    };
  },
  created() {},
  methods: {
    readDetails() {

    },
  },
};
</script>

<style scoped>
</style>