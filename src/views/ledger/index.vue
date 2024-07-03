<template>
  <div id="ledger" v-if="flag">
    <div class="overview">
      <div class="title">总览</div>
      <div>
        <div class="item" v-for="(item, index) in account" :key="index">
          {{ item.name + "：" + Math.round(item.amount * 100) / 100 + "元" }}
        </div>
      </div>
    </div>
    <el-divider class="divider" />
    <div class="record">
      <div class="title">详情</div>
      <el-button
        class="addButton"
        link
        type="primary"
        @click="openRecordDetailDialog('add', null)"
      >
        新增
      </el-button>
      <el-table class="table" :data="recordCurrent">
        <el-table-column
          prop="ledgerRecordTypeId"
          label="类型"
          :formatter="formatType"
          width="48px"
        />
        <el-table-column prop="amount" label="金额" width="78px" />
        <el-table-column
          prop="time"
          label="时间"
          :formatter="formatTime"
          width="98px"
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
        <el-form-item class="form-item" label="类型">
          <el-select
            class="ledgerRecordTypeId"
            v-model="recordEntity.ledgerRecordTypeId"
            placeholder="请选择类型"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          >
            <el-option
              v-for="(item, index) in recordType"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
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
        <el-form-item class="form-item" label="账号">
          <el-select
            class="ledgerAccountId"
            v-model="recordEntity.ledgerAccountId"
            placeholder="请选择账号"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          >
            <el-option
              v-for="(item, index) in account"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-button
            class="button"
            v-if="recordDetailDialogType != 'select'"
            @click="openAccountManagerDialog"
            >账号管理
          </el-button>
        </el-form-item>
        <el-form-item class="form-item" label="金额">
          <el-input-number
            class="amount"
            v-model="recordEntity.amount"
            :controls="false"
            :min="0"
            :precision="2"
            placeholder="请输入金额"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
        <el-form-item class="form-item" label="标签">
          <el-select
            class="ledgerLabelId"
            v-model="recordEntity.ledgerLabelId"
            placeholder="请选择标签"
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          >
            <el-option
              v-for="(item, index) in recordLabel"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-button
            class="button"
            v-if="recordDetailDialogType != 'select'"
            @click="openLabelManagerDialog"
            >标签管理
          </el-button>
        </el-form-item>
        <el-form-item class="form-item" label="备注">
          <el-input
            v-model="recordEntity.description"
            :rows="2"
            type="textarea"
            :placeholder="
              recordDetailDialogType === 'select' ? '' : '请输入备注'
            "
            :disabled="recordDetailDialogType === 'select'"
            size="small"
          />
        </el-form-item>
      </el-form>
      <div class="control">
        <div class="add" v-if="recordDetailDialogType === 'add'">
          <el-button type="primary" @click="addRecord"> 新增 </el-button>
        </div>
        <div class="update" v-else-if="recordDetailDialogType === 'update'">
          <el-button type="primary" @click="updateRecord"> 确定 </el-button>
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
    <el-dialog
      class="accountManagerDialog"
      v-model="accountManagerDialogVisible"
      :close-on-click-modal="false"
      title="账号管理"
      width="80%"
    >
      <el-form class="form">
        <el-form-item class="form-item" :model="accountEntity" label="标签">
          <el-input
            class="name"
            v-model="accountEntity.name"
            placeholder="请输入账号"
          />
        </el-form-item>
        <el-button class="button" type="primary" @click="addAccount"
          >新增账号</el-button
        >
      </el-form>
      <el-divider class="divider" />
      <div class="list">
        <draggable v-model="account" @end="updateAccountSort">
          <template #item="{ element }">
            <div class="item">
              {{ element.name }}
              <span class="button" @click="deleteAccount(element.id)">×</span>
            </div>
          </template>
        </draggable>
      </div>
    </el-dialog>
    <el-dialog
      class="labelManagerDialog"
      v-model="labelManagerDialogVisible"
      :close-on-click-modal="false"
      title="标签管理"
      width="80%"
    >
      <el-form class="form" :model="recordLabelEntity">
        <el-form-item class="form-item" label="标签">
          <el-input
            class="name"
            v-model="recordLabelEntity.name"
            placeholder="请输入标签"
          />
        </el-form-item>
        <el-button class="button" type="primary" @click="addLabel"
          >新增标签</el-button
        >
      </el-form>
      <el-divider class="divider" />
      <div class="list">
        <draggable v-model="recordLabel" @end="updateLabelSort">
          <template #item="{ element }">
            <div class="item">
              {{ element.name }}
              <span class="button" @click="deleteLabel(element.id)">×</span>
            </div>
          </template>
        </draggable>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import {
  deleteById,
  insertOne,
  selectAll,
  updateById,
  updateByIds,
} from "@/dao";
import { ElMessageBox } from "element-plus";
export default {
  name: "Ledger",
  components: {
    draggable,
  },
  data() {
    return {
      account: [],
      recordLabel: [],
      recordType: [],
      record: [],

      recordCurrent: [],

      pageSize: 5,
      currentPage: 1,

      recordEntity: null,
      accountEntity: null,
      recordLabelEntity: null,

      recordDetailDialogVisible: false,
      accountManagerDialogVisible: false,
      labelManagerDialogVisible: false,

      recordDetailDialogType: null,

      flag: false,
    };
  },
  created() {
    this.initRecordEntity();
    this.initAccountEntity();
    this.initRecordLabelEntity();

    Promise.all([
      this.getAccount(),
      this.getRecordLabel(),
      this.getRecordType(),
    ]).then(() => {
      Promise.all([this.getRecord()]).then(() => {
        this.flag = true;
      });
    });
  },
  methods: {
    // 新增记录
    addRecord() {
      this.recordEntity.time = this.recordEntity.time.toString();
      insertOne("ledgerRecord", this.recordEntity)
        .then((res) => {
          this.getRecord();
          this.recordDetailDialogVisible = false;
          this.$message.success("新增成功");
        })
        .catch((err) => {
          this.$message.success(err.err);
          console.log(err);
        });
    },
    // 新增账号
    addAccount() {
      Promise.all([this.updateAccountSort()]).then(() => {
        this.accountEntity.sort = this.account.length + 1;
        insertOne("ledgerAccount", this.accountEntity)
          .then((res) => {
            this.getAccount();
            this.initAccountEntity();
            this.$message.success("新增成功");
          })
          .catch((err) => {
            this.$message.error(err.err.toString());
            console.log(err);
          });
      });
    },
    // 新增标签
    addLabel() {
      Promise.all([this.updateLabelSort()]).then(() => {
        this.recordLabelEntity.sort = this.recordLabel.length + 1;
        insertOne("ledgerRecordLabel", this.recordLabelEntity)
          .then((res) => {
            this.getRecordLabel();
            this.initRecordLabelEntity();
            this.$message.success("新增成功");
          })
          .catch((err) => {
            this.$message.error(err.err.toString());
            console.log(err);
          });
      });
    },

    // 删除记录
    deleteRecord() {
      ElMessageBox.confirm("确定删除？").then(() => {
        deleteById("ledgerRecord", this.recordEntity.id)
          .then((res) => {
            this.getRecord();
            this.recordDetailDialogVisible = false;
            this.$message.success("删除成功");
          })
          .catch(() => {});
      });
    },
    // 删除账号
    deleteAccount(id) {
      for (let i = 0; i < this.record.length; i++) {
        if (this.record[i].ledgerAccountId === id) {
          this.$message.error("此账号被使用，无法删除");
          return;
        }
      }
      this.recordEntity.ledgerAccountId = null;
      deleteById("ledgerAccount", id)
        .then((res) => {
          this.getAccount();
          this.$message.success("删除成功");
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 删除标签
    deleteLabel(id) {
      for (let i = 0; i < this.record.length; i++) {
        if (this.record[i].ledgerLabelId === id) {
          this.$message.error("此标签被使用，无法删除");
          return;
        }
      }
      this.recordEntity.ledgerLabelId = null;
      deleteById("ledgerRecordLabel", id)
        .then((res) => {
          this.getRecordLabel();
          this.$message.success("删除成功");
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 更新记录
    updateRecord() {
      this.recordEntity.time = this.recordEntity.time.toString();
      updateById("ledgerRecord", this.recordEntity)
        .then((res) => {
          this.recordEntity.time = new Date(this.recordEntity.time);
          this.openRecordDetailDialog("select", this.recordEntity);
          this.getRecord();
          this.$message.success("更新成功");
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 修改账号顺序
    updateAccountSort() {
      for (let i = 0; i < this.account.length; i++) {
        this.account[i].sort = i + 1;
        delete this.account[i]["amount"];
      }
      return updateByIds("ledgerAccount", this.account)
        .then((res) => {
          this.getAccount();
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 修改标签顺序
    updateLabelSort() {
      for (let i = 0; i < this.recordLabel.length; i++) {
        this.recordLabel[i].sort = i + 1;
      }
      return updateByIds("ledgerRecordLabel", this.recordLabel)
        .then((res) => {
          this.getRecordLabel();
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 获取账号列表
    getAccount() {
      return selectAll("ledgerAccount")
        .then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            res.data[i]["amount"] = 0;
          }
          this.account = res.data.sort((a, b) => a.sort - b.sort);
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 获取账本记录标签
    getRecordLabel() {
      return selectAll("ledgerRecordLabel")
        .then((res) => {
          this.recordLabel = res.data.sort((a, b) => a.sort - b.sort);
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 获取账本记录类型
    getRecordType() {
      return selectAll("ledgerRecordType")
        .then((res) => {
          this.recordType = res.data;
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },
    // 获取账本记录列表
    getRecord() {
      return selectAll("ledgerRecord")
        .then((res) => {
          this.record = res.data;
          this.record.sort((a, b) => {
            return new Date(b.time) - new Date(a.time);
          });
          for (let i = 0; i < this.account.length; i++) {
            this.account[i]["amount"] = 0;
          }
          for (let i = this.record.length - 1; i >= 0; i--) {
            this.record[i].time = new Date(this.record[i].time);
            for (let j = 0; j < this.account.length; j++) {
              if (this.account[j]["id"] === this.record[i]["ledgerAccountId"]) {
                switch (this.record[i]["ledgerRecordTypeId"]) {
                  case 1:
                    this.account[j]["amount"] -= this.record[i]["amount"];
                    break;
                  case 2:
                    this.account[j]["amount"] += this.record[i]["amount"];
                    break;
                  case 3:
                    this.account[j]["amount"] = this.record[i]["amount"];
                    break;
                  default:
                    this.$message.error(
                      "未知账本记录类型主键" +
                        this.record[i]["ledgerRecordTypeId"]
                    );
                }
                break;
              }
            }
          }
          this.changeCurrentPage();
        })
        .catch((err) => {
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 初始化recordEntity
    initRecordEntity() {
      this.recordEntity = {
        ledgerRecordTypeId: null,
        amount: 0,
        time: new Date(),
        ledgerLabelId: null,
      };
    },
    // 初始化accountEntity
    initAccountEntity() {
      this.accountEntity = {
        name: null,
      };
    },
    // 初始化recordLabelEntity
    initRecordLabelEntity() {
      this.recordLabelEntity = {
        name: null,
      };
    },

    // 打开记录详情对话框
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
    // 打开账号管理对话框
    openAccountManagerDialog() {
      this.initAccountEntity();
      this.accountManagerDialogVisible = true;
    },
    // 打开标签管理对话框
    openLabelManagerDialog() {
      this.initRecordLabelEntity();
      this.labelManagerDialogVisible = true;
    },

    // 格式化类型
    formatType(row, column, val, index) {
      return this.getTypeItemById(val);
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

    // 根据id获取类型对象
    getTypeItemById(id) {
      for (let i = 0; i < this.recordType.length; i++) {
        if (this.recordType[i].id === id) {
          return this.recordType[i].name;
        }
      }
      this.$message.error("id为" + id + "的类型对象不存在");
    },
    // 根据id获取标签对象
    getLabelItemById(id) {
      for (let i = 0; i < this.recordLabel.length; i++) {
        if (this.recordLabel[i].id === id) {
          return this.recordLabel[i];
        }
      }
      this.$message.error("id为" + id + "的标签对象不存在");
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
.title {
  font-size: 18px;
  font-weight: bold;
}

#ledger .overview .item {
  margin: 2px 0px 0px 0px;

  font-size: 15px;
}

#ledger .divider {
  margin: 8px 0px 8px 0px;
  border-color: rgb(159, 154, 154);
}

#ledger .record .addButton {
  margin: 5px 0px 5px 0px;

  font-size: 16px;
}

#ledger .record .table {
  font-size: 12px;
}

#ledger .record .pagination {
  justify-content: center;

  margin: 3px 0px 0px 0px;
}

#ledger .recordDetailDialog .form .form-item {
  margin: 2px;
}

#ledger .recordDetailDialog .form .form-item .ledgerRecordTypeId {
  width: 120px;
}

#ledger .recordDetailDialog .form .form-item .ledgerAccountId {
  width: 120px;
}

#ledger .recordDetailDialog .form .form-item .amount {
  width: 120px;
}

#ledger .recordDetailDialog .form .form-item .amount /deep/ .el-input__inner {
  text-align: left;
}

#ledger .recordDetailDialog .form .form-item .ledgerLabelId {
  width: 120px;
}

#ledger .recordDetailDialog .form .form-item .button {
  height: 80%;
  width: 60px;
  margin: 0px 0px 0px 10px;

  font-size: 12px;
}

#ledger .recordDetailDialog .control {
  text-align: right;
}

#ledger .accountManagerDialog .form .form-item .name {
  width: 120px;
}

#ledger .accountManagerDialog .form .button {
  width: 80px;
  height: 30px;
}

#ledger .accountManagerDialog .list .item {
  display: inline-block;

  margin: 2px 2px 2px 2px;
  padding: 3px 6px 3px 6px;
  border-radius: 8px;
  background-color: aquamarine;

  user-select: none;
}

#ledger .accountManagerDialog .list .item .button {
  cursor: pointer;
}

#ledger .labelManagerDialog .form .form-item .name {
  width: 120px;
}

#ledger .labelManagerDialog .form .button {
  width: 80px;
  height: 30px;
}

#ledger .labelManagerDialog .list .item {
  display: inline-block;

  margin: 2px 2px 2px 2px;
  padding: 3px 6px 3px 6px;
  border-radius: 8px;
  background-color: aquamarine;

  user-select: none;
}

#ledger .labelManagerDialog .list .item .button {
  cursor: pointer;
}
</style>