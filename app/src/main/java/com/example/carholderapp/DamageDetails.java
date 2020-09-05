package com.example.carholderapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Calendar;
public class DamageDetails {
    public String DamageType;
    public String ImageList;
    public String ImageListDetected;
    public String Location;
    public String meterReaing;
    public String Status;
    public String Damage;
    public String Estimation;
    public String ClaimNo;
    public String Date;
    public String currentTime;
    public String Name;
    public String ClaimeAmount;

    public DamageDetails(String DamageType,String ImageList,String Location,String meterReaing,String Status,String Damage,String Estimation,String ClaimNo,String Date,String currentTime,String ImageListDetected,String Name,String ClaimeAmount) {

            this.Date=Date;
            this.ImageListDetected=ImageListDetected;
            this.currentTime=currentTime;
            this.DamageType=DamageType;
            this.ImageList=ImageList;
            this.Location=Location;
            this.meterReaing=meterReaing;
            this.Status=Status;
            this.Damage=Damage;
            this.Estimation=Estimation;
            this.ClaimNo=ClaimNo;
            this.Name=Name;
            this.ClaimeAmount=ClaimeAmount;
    }
}
