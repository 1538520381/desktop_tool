const AdmZip = require('adm-zip');

const WhiteList = [
    "ledgerRecordType",
]

// 检查文件名是否匹配白名单
function isWhiteListFile(filePath) {
    const fileName = filePath.split(/[/\\]/).pop();
    return WhiteList.some(name => {
        const pattern = new RegExp(`^(#?)${name}(\\.json)(\\.temp)?$`, 'i');
        return pattern.test(fileName);
    });
}

// 解压
export function decompress(sourcePath, exportPath) {
    try {
        const zip = new AdmZip(sourcePath);
        const entries = zip.getEntries();
        const entriesToExtract = entries.filter(entry => !isWhiteListFile(entry.entryName));
        entriesToExtract.forEach(entry => {
            zip.extractEntryTo(entry, exportPath, true, true);
        });
        return {
            code: 0
        }
    } catch (err) {
        return {
            code: 1,
            err: err
        }
    }
}

// 压缩
export function compress(sourcePath, exportPath) {
    try {
        const zip = new AdmZip();
        zip.addLocalFolder(sourcePath, '', (file) => !isWhiteListFile(file));
        zip.writeZip(exportPath);
        return {
            code: 0
        }
    } catch (err) {
        return {
            code: 1,
            err: err
        }
    }
}