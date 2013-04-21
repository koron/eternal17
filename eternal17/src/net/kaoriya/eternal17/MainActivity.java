package net.kaoriya.eternal17;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity
{
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
        // TODO: configurable birth day.
        Date birthDay = new GregorianCalendar(1970, 1, 1).getTime();
        setAge(calcAge(birthDay));
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
}
