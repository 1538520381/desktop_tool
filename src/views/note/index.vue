<template>
  <div id="note">
    <el-scrollbar height="600px">
      <div class="item" v-for="(item, index) in note" :key="index">
        <input class="title" v-model="item.title" placeholder="主题">
        <div class="buttons">
          <svg-icon class="button" icon-class="refresh" v-if="openIds.indexOf(item.id) !== -1"
                    @click="getNote"></svg-icon>
          <svg-icon class="button" icon-class="tick" v-if="index === 0 || openIds.indexOf(item.id) !== -1"
                    @click="index === 0 ? addNote(item) : updateNote(item)"></svg-icon>
          <svg-icon class="button" icon-class="delete" v-if="index !== 0" @click="deleteNote(item.id)"></svg-icon>
          <svg-icon class="button" icon-class="arrowUp" v-if="openIds.indexOf(item.id) !== -1"
                    @click="openIds.splice(openIds.indexOf(item.id),1)"></svg-icon>
          <svg-icon class="button" icon-class="arrowDown" v-else-if="index !== 0"
                    @click="openIds.push(item.id)"></svg-icon>
        </div>
        <textarea
            class="content"
            v-model="item.content"
            placeholder="内容"
            @input="autoHeight"
            v-if="index === 0 || openIds.indexOf(item.id) !== -1"
        ></textarea>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
import {deleteById, insertOne, selectAll, updateById} from "@/dao";
import SvgIcon from "@/components/svgIcon/index.vue";
import {ElMessageBox} from "element-plus";

export default {
  name: "Note",
  components: {SvgIcon},
  data() {
    return {
      note: [],

      openIds: []
    };
  },
  created() {
    this.getNote();
  },
  methods: {
    // 新增笔记
    addNote(item) {
      insertOne("note", item).then((res) => {
        this.getNote()
      }).catch((err) => {
        this.$message.error(err.err.toString())
        console.log((err))
      })
    },

    // 删除笔记
    deleteNote(id) {
      ElMessageBox.confirm("确定删除？").then(() => {
        deleteById("note", id).then((res) => {
          this.getNote()
        })
      }).catch((err) => {
        this.$message.error(err.err.toString())
        console.log(err)
      })
    },

    // 修改笔记
    updateNote(item) {
      updateById("note", item).then((res) => {
        this.getNote()
      }).catch((err) => {
        this.$message.error(err.err.toString())
        console.log(err)
      })
    },

    // 获取所有笔记
    getNote() {
      selectAll("note")
          .then((res) => {
            this.note = res.data.sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime());
            this.note.unshift({});
          })
          .catch((err) => {
            this.$message.error(err.err.toString());
            console.log(err);
          });
    },

    // 文本域高度自适应
    autoHeight(e) {
      console.log(e.target.style);
      e.target.style.height = "0px"
      e.target.style.height = e.target.scrollHeight + "px";
    },
  },
};
</script>

<style scoped>
#note .item {
  margin: 0 0 6px 0;
  padding: 5px 10px 5px 10px;
  border-radius: 5px;

  background: white;
}

#note .item .title {
  width: calc(100% - 18px * 4);
  height: 30px;
  margin: 0;
  padding: 0;

  border: none;
  outline: none;

  font-size: 15px;
}

#note .item .buttons {
  display: inline-block;

  float: right;
}

#note .item .buttons .button {
  width: 18px;
  height: 27px;

  vertical-align: middle;

  cursor: pointer;
}

#note .item .content {
  width: 100%;
  min-height: 80px;
  margin: 0;
  padding: 0;

  border: none;
  outline: none;

  font-size: 15px;

  resize: none;
  overflow: hidden;
  transition: transform 0.3s ease;
}
</style>