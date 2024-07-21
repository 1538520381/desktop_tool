<template>
  <div id="todo">
    <div class="record">
      <el-button
        class="textButton"
        link
        type="primary"
        @click="openRecordDetailDialog('add', null)"
      >
        新增
      </el-button>
      <el-table class="table" :data="recordCurrent">
        <el-table-column class="status" prop="status" label="状态" width="48px">
          <template #default="scope">
            <svg-icon
              class="checkbox"
              v-if="!scope.row.status"
              icon-class="checkbox_false"
              @click="updateRecordStatus(scope.row, true)"
            />
            <svg-icon
              class="checkbox"
              v-else
              icon-class="checkbox_true"
              @click="updateRecordStatus(scope.row, false)"
            />
          </template>
        </el-table-column>
        <el-table-column class="name" prop="name" label="事项" width="84px">
          <template #default="scope">
            <el-text class="name_true" v-if="scope.row.status" tag="del">{{
              scope.row.name
            }}</el-text>
            <el-text class="name_false" v-else>{{ scope.row.name }}</el-text>
          </template>
        </el-table-column>
        <el-table-column
          prop="time"
          label="时间"
          :formatter="formatTime"
          width="92px"
        />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="openRecordDetailDialog('select', scope.row)"
              >详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        small
        layout="prev, pager, next"
        :total="record.length"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="changeCurrentPage"
      />
    </div>
    <el-dialog
      class="recordDetailDialog"
      v-model="recordDetailDialogVisible"
      :close-on-click-modal="false"
      :title="getRecordDetailDialogTitle"
      width="80%"
    >
      <el-form class="form" :model="recordEntity">
        <el-form-item class="form-item" label="事项">
          <el-input
            class="name"
            v-model="recordEntity.name"
            placeholder="请输入事项"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
        <el-form-item class="form-item" label="时间">
          <el-date-picker
            class="time"
            v-model="recordEntity.time"
            type="datetime"
            placeholder="请输入时间"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
        <el-form-item class="form-item" label="详情">
          <el-input
            v-model="recordEntity.remark"
            :rows="2"
            type="textarea"
            :placeholder="
              recordDetailDialogType === 'select' ? '' : '请输入详情'
            "
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
        <el-form-item class="form-item" label="状态">
          <el-switch
            class="status"
            v-model="recordEntity.status"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
      </el-form>
      <div class="control">
        <div class="add" v-if="recordDetailDialogType === 'add'">
          <el-button class="button" type="primary" @click="addRecord">
            新增
          </el-button>
        </div>
        <div class="update" v-else-if="recordDetailDialogType === 'update'">
          <el-button type="primary" @click="updateRecord(recordEntity)">
            确定
          </el-button>
        </div>
        <div class="update" v-else-if="recordDetailDialogType === 'select'">
          <el-button type="danger" @click="deleteRecord"> 删除 </el-button>
          <el-button
            type="primary"
            @click="openRecordDetailDialog('update', null)"
          >
            修改
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deleteById, insertOne, selectAll, updateById } from "@/dao";
import { ElMessageBox } from "element-plus";
export default {
  name: "Todo",
  data() {
    return {
      record: [],

      recordCurrent: [],

      recordEntity: {},

      recordDetailDialogVisible: false,

      recordDetailDialogType: "add",

      currentPage: 1,
      pageSize: 10,
    };
  },
  created() {
    this.getRecord();
  },
  methods: {
    // 新增待做记录
    addRecord() {
      this.recordEntity.time = this.recordEntity.time.toString();
      insertOne("todoRecord", this.recordEntity)
        .then((res) => {
          this.getRecord();
          this.$message.success("新增成功");
          this.recordDetailDialogVisible = false;
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 删除待做记录
    deleteRecord() {
      ElMessageBox.confirm("确定删除？")
        .then(() => {
          deleteById("todoRecord", this.recordEntity.id)
            .then((res) => {
              this.getRecord();
              this.recordDetailDialogVisible = false;
              this.$message.success("删除成功");
            })
            .catch(() => {});
        })
        .catch(() => {});
    },

    // 修改待做记录
    updateRecord(item) {
      item.time = item.time.toString();
      updateById("todoRecord", item)
        .then((res) => {
          this.getRecord();
          this.recordDetailDialogVisible = false;
          this.$message.success("修改成功");
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 获取待做记录
    getRecord() {
      return selectAll("todoRecord")
        .then((res) => {
          this.record = res.data;
          this.record.sort((a, b) => {
            if (a.status && b.status) {
              return new Date(b.time) - new Date(a.time);
            } else if (!a.status && !b.status) {
              return new Date(a.time) - new Date(b.time);
            }
            return a.status ? 1 : -1;
          });
          for (let i = 0; i < this.record.length; i++) {
            this.record[i].time = new Date(this.record[i].time);
          }
          this.changeCurrentPage();
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 修改待做记录状态
    updateRecordStatus(item, status) {
      item.status = status;
      this.updateRecord(JSON.parse(JSON.stringify(item)));
    },

    // 初始化待做记录详情实体类
    initRecordEntity() {
      this.recordEntity = {
        name: "",
        time: new Date(),
        remark: "",
        status: false,
      };
    },

    // 打开待做记录详情对话框
    openRecordDetailDialog(type, entity) {
      if (type === "add") {
        this.initRecordEntity();
      } else if (type === "update") {
      } else if (type === "select") {
        this.recordEntity = JSON.parse(JSON.stringify(entity));
      }
      this.recordDetailDialogType = type;
      this.recordDetailDialogVisible = true;
    },

    // 格式化时间
    formatTime(row, column, val, index) {
      return (
        val.getMonth() +
        1 +
        "-" +
        val.getDate() +
        " " +
        (val.getHours().toString()[1]
          ? val.getHours().toString()
          : "0" + val.getHours().toString()) +
        ":" +
        (val.getMinutes().toString()[1]
          ? val.getMinutes().toString()
          : "0" + val.getMinutes().toString())
      );
    },

    // 换页
    changeCurrentPage() {
      this.recordCurrent = this.record.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    },
  },
  computed: {
    // 获取待做记录详情对话框标题
    getRecordDetailDialogTitle() {
      switch (this.recordDetailDialogType) {
        case "add":
          return "新增记录";
        case "update":
          return "修改记录";
        case "select":
          return "记录详情";
        default:
          return "";
      }
    },
  },
};
</script>

<style scoped>
#todo .record .textButton {
  margin: 5px 2px 5px 0px;

  font-size: 16px;
}

#todo .record .table {
  font-size: 12px;
}

.checkbox {
  width: 16px;
  height: 16px;
}

.name_true {
  font-size: 10px;

  color: gray;
}

.name_false {
  font-size: 10px;
}

#todo .record .pagination {
  justify-content: center;

  margin: 3px 0px 0px 0px;
}

#todo .recordDetailDialog .form .form-item {
  margin: 2px 2px 2px 2px;
}

#todo .recordDetailDialog .form .form-item .status {
  --el-switch-on-color: #13ce66;
  --el-switch-off-color: #ff4949;
}

#todo .recordDetailDialog .control {
  text-align: right;
}

#todo .recordDetailDialog .control .button {
  height: 26px;
}
</style>