const AdmZip = require('adm-zip');

// 解压
export function decompress(sourcePath, exportPath) {
    try {
        const zip = new AdmZip(sourcePath);
        zip.extractAllTo(exportPath, true);
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
        zip.addLocalFolder(sourcePath);
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