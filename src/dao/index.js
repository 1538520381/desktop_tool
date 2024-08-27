import {read, write, copy} from "@/utils/IOUtil"
import {isEmpty} from "@/utils/validate"

const databasePath = "/src/data/"
const autoFunctionMap = {
    'autoIncrement': autoIncrement, 'currentTime': currentTime
}


// 自动补齐
// 自增
function autoIncrement(template, item, field) {
    let val = parseInt(template[field]['auto'].substring('autoIncrement'.length + 1))
    template[field]['auto'] = 'autoIncrement' + ':' + (val + 1)
    item[field] = val
}

// 当前时间
function currentTime(template, item, field) {
    item[field] = new Date()
}

// 效验
// 根据模板效验对象
function checkItem(item, template) {
    for (let itemKey in item) {
        if (template[itemKey] === undefined) {
            throw {
                code: 1, err: '数据库无字段' + itemKey
            }
        } else if (typeof (item[itemKey]) != template[itemKey]['type']) {
            throw {
                code: 1,
                err: '字段' + '\"' + template[itemKey]['description'] + '\"' + '数据类型有误，需要数据类型' + template[itemKey]['type']
            }
        }
    }
    for (let templateKey in template) {
        if (isEmpty(item[templateKey])) {
            if (isEmpty(template[templateKey]['auto'])) {
                if (!template[templateKey]['null']) {
                    throw {
                        code: 1, err: '字段' + '\"' + template[templateKey]['description'] + '\"' + '不能为空'
                    }
                }
            } else {
                for (let key in autoFunctionMap) {
                    if (template[templateKey]['auto'].indexOf(key) === 0) {
                        autoFunctionMap[key](template, item, templateKey)
                    }
                }
            }
        }
    }
}

// 查询条件字段效验查询条件
function checkField(queryWrapper, fields) {
    for (let i = 0; i < fields.length; i++) {
        if (isEmpty(queryWrapper[fields[i]])) {
            throw {
                code: 1, err: new Error('缺少字段' + fields[i])
            }
        }
    }
}

// 根据查询条件效验对象
function checkByQuery(item, queryWrapper) {
    try {
        checkField(queryWrapper, ['operator'])
        switch (queryWrapper['operator']) {
            case '=':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] === queryWrapper['value']
            case '>':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] > queryWrapper['value']
            case '>=':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] >= queryWrapper['value']
            case '<':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] < queryWrapper['value']
            case '<=':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] <= queryWrapper['value']
            case '!=':
                checkField(queryWrapper, ['field', 'value'])
                return item[queryWrapper['field']] != queryWrapper['value']
            case 'like':
                checkField(queryWrapper, ['field', 'value'])
                if (typeof (item[queryWrapper['field']]) != 'string') {
                    throw {
                        code: 1, err: new Error('只能对字符串类型字段进行模糊查询')
                    }
                }
                return item[queryWrapper['field']].indexOf(queryWrapper['value']) >= 0
            case '&':
                checkField(queryWrapper, ['condition1', 'condition2'])
                return checkByQuery(item, queryWrapper['condition1']) && checkByQuery(item, queryWrapper['condition2'])
            case '|':
                checkField(queryWrapper, ['condition1', 'condition2'])
                return checkByQuery(item, queryWrapper['condition1']) || checkByQuery(item, queryWrapper['condition2'])
            default:
                throw {
                    code: 1, err: new Error('未知运算符')
                }
        }
    } catch (err) {
        throw {
            code: 1, err: err.err
        }
    }
}

// 增
// 增加单个
export function insertOne(dataName, item) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            checkItem(item, res1.data[0])
            res1.data.push(item)
            copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                write(databasePath + dataName + ".json", res1.data).then((res3) => {
                    resolve({
                        code: 0
                    })
                }).catch((err) => {
                    copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {
                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    reject({
                        code: 1, err: err.err
                    })
                })
            }).catch((err) => {
                reject({
                    code: 1, err: err.err
                })
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 增加多个
export function insertList(dataName, items) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            for (let i = 0; i < items.length; i++) {
                checkItem(items[i], res1.data[0])
                res1.data.push(items[i])
            }
            copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                write(databasePath + dataName + ".json", res1.data).then((res3) => {
                    resolve({
                        code: 0
                    })
                }).catch((err) => {
                    copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {

                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    reject({
                        code: 1, err: err.err
                    })
                })
            }).catch((err) => {
                reject({
                    code: 1, err: err.err
                })
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 删
// 根据id删除
export function deleteById(dataName, id) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            for (let i = 1; i < res1.data.length; i++) {
                if (res1.data[i]['id'] === id) {
                    res1.data.splice(i, 1)
                    copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                        write(databasePath + dataName + ".json", res1.data).then((res3) => {
                            resolve({
                                code: 0
                            })
                        }).catch((err) => {
                            copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {

                            }).catch((err) => {
                                reject({
                                    code: 1, err: err.err
                                })
                            })
                            reject({
                                code: 1, err: err.err
                            })
                        })
                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    return;
                }
            }
            reject({
                code: 1, err: "无法在" + dataName + "表中查询到id为" + id + "的记录"
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 根据id列表删除
export function deleteByIds(dataName, ids) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            for (let i = 0; i < ids.length; i++) {
                let flag = false
                for (let j = 1; j < res1.data.length; j++) {
                    if (res1.data[j]['id'] === ids[i]) {
                        flag = true
                        res1.data.splice(j, 1)
                        break
                    }
                }
                if (!flag) {
                    reject({
                        code: 1, err: "无法在" + dataName + "表中查询到id为" + ids[i] + "的记录"
                    })
                    return;
                }
            }
            copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                write(databasePath + dataName + ".json", res1.data).then((res3) => {
                    resolve({
                        code: 0
                    })
                }).catch((err) => {
                    copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {

                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    reject({
                        code: 1, err: err.err
                    })
                })
            }).catch((err) => {
                reject({
                    code: 1, err: err.err
                })
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 改
// 根据含id对象修改
export function updateById(dataName, item) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            for (let i = 1; i < res1.data.length; i++) {
                if (res1.data[i]['id'] === item['id']) {
                    for (let itemKey in item) {
                        res1.data[i][itemKey] = item[itemKey]
                    }
                    checkItem(res1.data[i], res1.data[0])
                    copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                        write(databasePath + dataName + ".json", res1.data).then((res3) => {
                            resolve({
                                code: 0
                            })
                        }).catch((err) => {
                            copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {

                            }).catch((err) => {
                                reject({
                                    code: 1, err: err.err
                                })
                            })
                            reject({
                                code: 1, err: err.err
                            })
                        })
                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    return;
                }
            }
            reject({
                code: 1, err: "无法在" + dataName + "表中查询到id为" + item['id'] + "的记录"
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 根据含id对象列表修改
export function updateByIds(dataName, items) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res1) => {
            for (let i = 0; i < items.length; i++) {
                let flag = false
                for (let j = 1; j < res1.data.length; j++) {
                    if (res1.data[j]['id'] === items[i]['id']) {
                        flag = true
                        for (let itemKey in items[i]) {
                            res1.data[j][itemKey] = items[i][itemKey]
                        }
                        checkItem(res1.data[j], res1.data[0])
                        break
                    }
                }
                if (!flag) {
                    reject({
                        code: 1, err: "无法在" + dataName + "表中查询到id为" + items[i]['id'] + "的记录"
                    })
                    return;
                }
            }
            copy(databasePath + dataName + ".json", databasePath + dataName + ".json.temp").then((res2) => {
                write(databasePath + dataName + ".json", res1.data).then((res3) => {
                    resolve({
                        code: 0
                    })
                }).catch((err) => {
                    copy(databasePath + dataName + ".json.temp", databasePath + dataName + ".json").then((res3) => {

                    }).catch((err) => {
                        reject({
                            code: 1, err: err.err
                        })
                    })
                    reject({
                        code: 1, err: err.err
                    })
                })
            }).catch((err) => {
                reject({
                    code: 1, err: err.err
                })
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 查
// 查询所有
export function selectAll(dataName) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res) => {
            let data = res.data
            data.shift();
            resolve({
                code: 0, data: data
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        });
    });
}

// 根据id查询
export function selectById(dataName, id) {
    return new Promise((resolve, reject) => {
        read(databasePath + dataName + ".json").then((res) => {
            for (let i = 1; i < res.data.length; i++) {
                if (res.data[i]['id'] === id) {
                    resolve({
                        code: 0, data: res.data[i]
                    })
                    return;
                }
            }
            reject({
                code: 1, err: "无法在" + dataName + "表中查询到id为" + id + "的记录"
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}

// 根据查询条件查询
export function selectByQueryWrapper(dataName, queryWrapper) {
    return new Promise((resolve, reject) => {
        let data = []
        read(databasePath + dataName + ".json").then((res) => {
            for (let i = 1; i < res.data.length; i++) {
                if (checkByQuery(res.data[i], queryWrapper)) {
                    data.push(res.data[i])
                }
            }
            resolve({
                code: 0, data: data
            })
        }).catch((err) => {
            reject({
                code: 1, err: err.err
            })
        })
    })
}