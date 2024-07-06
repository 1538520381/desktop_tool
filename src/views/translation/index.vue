<template>
  <div id="translation">
    <el-select
      class="fromLanguage"
      v-model="fromLanguage"
      @change="translation"
    >
      <el-option
        v-for="(item, key) in IOS639"
        :key="key"
        :label="item"
        :value="key"
      />
    </el-select>
    <svg-icon
      class="exchange"
      icon-class="exchange"
      @click="exchangeLanguage"
    />
    <el-select class="fromLanguage" v-model="toLanguage" @change="translation">
      <el-option
        v-for="(item, key) in IOS639"
        :key="key"
        :label="item"
        :value="key"
      />
    </el-select>
    <el-input
      class="fromText"
      v-model="fromText"
      :rows="10"
      type="textarea"
      placeholder="请输入要翻译的文本"
      @input="translation"
    />
    <el-input
      class="toText"
      v-model="toText"
      :rows="10"
      type="textarea"
      placeholder="翻译结果"
      :readonly="true"
    />
  </div>
</template>

<script>
import IOS639 from "@/json/ISO639";
import md5 from "js-md5";
import request from "@/utils/request";
import { read } from "@/utils/IOUtil";

export default {
  name: "Translation",
  data() {
    return {
      IOS639: IOS639,

      fromLanguage: Object.keys(IOS639)[0],
      toLanguage: Object.keys(IOS639)[1],
      fromText: "",
      toText: "",

      appId: "",
      secretKey: "",

      timeout: null,
    };
  },
  methods: {
    // 获取百度翻译信息
    getBaidu() {
      return read("/src/json/setup.json")
        .then((res) => {
          this.appId = res.data.baiduAppId;
          this.secretKey = res.data.baiduSecretKey;
        })
        .catch((err) => {
          this.abc = 2;
          this.$message.error(err.err.toString());
          console.log(err);
        });
    },

    // 互换切换语言
    exchangeLanguage() {
      let temp = this.fromLanguage;
      this.fromLanguage = this.toLanguage;
      this.toLanguage = temp;
      this.translation();
    },

    // 翻译
    translation() {
      if (this.timeout == null) {
        let _this = this;
        this.timeout = setTimeout(function () {
          clearTimeout(_this.timeout);
          _this.timeout = null;
          if (_this.fromText != "") {
            Promise.all([_this.getBaidu()]).then(() => {
              let salt = new Date().getTime();
              let sign = md5(
                _this.appId + _this.fromText + salt + _this.secretKey
              );
              request({
                url: "https://fanyi-api.baidu.com/api/trans/vip/translate",
                method: "POST",
                params: {
                  q: _this.fromText,
                  from: _this.fromLanguage,
                  to: _this.toLanguage,
                  appid: _this.appId,
                  salt: salt,
                  sign: sign,
                },
              })
                .then((res) => {
                  ``;
                  if (res.data.error_code !== undefined) {
                    _this.abc = 3;
                    _this.$message.error(res.data.error_msg);
                    console.log(res.data);
                  } else if (_this.fromText === "") {
                    _this.toText = "";
                  } else {
                    _this.toText = res.data.trans_result[0].dst;
                  }
                })
                .catch((err) => {
                  _this.$message.error(err.err.toString());
                  console.log(err);
                });
            });
          } else {
            _this.toText = "";
          }
        }, 200);
      }
    },
  },
};
</script>

<style scoped>
#translation .fromLanguage {
  width: 30%;
}

#translation .exchange {
  vertical-align: middle;

  width: 15px;
  height: 15px;
  margin: 0px 2px 0px 2px;

  cursor: pointer;
}

#translation .toLanguage {
  width: 30%;
}

#translation .fromText {
  margin: 5px 0px 0px 0px;
}

#translation .toText {
  min-height: 200px;
  margin: 3px 0px 0px 0px;
}
</style>