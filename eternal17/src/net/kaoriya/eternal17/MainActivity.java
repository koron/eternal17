package net.kaoriya.eternal17;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.util.Log;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.DatePicker;

public class MainActivity extends Activity
{
    public final static String TAG = "eternal17";
    public final static String PREF_SETTINGS = "settings";
    public final static String BIRTH_DAY = "birth_day";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Date birthDay = getBirthDay();
        if (birthDay != null) {
            setAge(calcAge(birthDay));
        } else {
            chooseBirthDay();
        }
    }

    private void setAge(Age age)
    {
        if (age == null) {
            age = new Age();
        }
        TextView textYears = (TextView)findViewById(R.id.text_years);
        textYears.setText(getString(R.string.format_years, age.years));
        TextView textDays = (TextView)findViewById(R.id.text_days);
        textDays.setText(getString(R.string.format_days, age.days));
    }

    public static Age calcAge(Date birthDay)
    {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(birthDay);
        c.add(Calendar.YEAR, 17);
        int days = getDays(new Date(), c.getTime());
        return new Age(17, days);
    }

    public static int getDays(Date d1, Date d0)
    {
        long diff = d1.getTime() - d0.getTime();
        return (int)(diff / 86400000L);
    }

    private Date getBirthDay()
    {
        SharedPreferences pref =
            getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE);
        long value = pref.getLong(BIRTH_DAY, -1);
        if (value < 0) {
            return null;
        }
        return new Date(value);
    }

    private void setBirthDay(Date date)
    {
        SharedPreferences.Editor edit =
            getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE).edit();
        edit.putLong(BIRTH_DAY, date.getTime());
        edit.commit();
    }

    private void applyBirthDay(Date date)
    {
        setBirthDay(date);
        setAge(calcAge(date));
    }

    private void chooseBirthDay()
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -17);
        DatePickerDialog d = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(
                        DatePicker view,
                        int year,
                        int monthOfYear,
                        int dayOfMonth)
                    {
                        applyBirthDay(new GregorianCalendar(year,
                                monthOfYear, dayOfMonth).getTime());
                    }
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        d.show();
    }
}
