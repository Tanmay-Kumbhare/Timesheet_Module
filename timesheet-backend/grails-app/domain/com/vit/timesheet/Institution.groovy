package com.vit.timesheet

class Institution {
    String name
    String code
    Boolean isActive = true

    static constraints = {
        name    blank: false, maxSize: 255
        code    blank: false, maxSize: 50, unique: true
        isActive nullable: false
    }

    static mapping = {
        table 'institution'
    }
}