package com.vit.timesheet

class Department {
    String      name
    String      code
    Institution institution
    Boolean     isActive = true

    static belongsTo = [institution: Institution]

    static constraints = {
        name        blank: false, maxSize: 255
        code        blank: false, maxSize: 50, unique: true
        institution nullable: false
        isActive    nullable: false
    }

    static mapping = {
        table 'department'
    }
}
