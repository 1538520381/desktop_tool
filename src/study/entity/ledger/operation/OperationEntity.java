package study.entity.ledger.operation;

/**
 * @author Persolute
 * @version 1.0
 * @description 操作区实体类
 * @email 1538520381@qq.com
 * @date 2024/3/2 21:12
 */
public class OperationEntity {
    private String year;
    private String month;
    private String day;
    private int type;
    private int account;
    private String amount;
    private String description;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
