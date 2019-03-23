package br.com.nocaute.pojos;

public class Genre {
	private String code;
    private String description;
    
    public Genre(String code, String description) {
    	setCode(code);
    	setDescription(description);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
