package net.kaoriya.eternal17;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.reset_birthday:
                setAge(null);
                setBirthDay(null);
                chooseBirthDay();
                return true;
        }
        return false;
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
        if (date != null) {
            edit.putLong(BIRTH_DAY, date.getTime());
        } else {
            edit.remove(BIRTH_DAY);
        }
        edit.commit();
    }

    private void applyBirthDay(Date date)
    {
        if (date == null) {
            date = getBirthDay();
            if (date == null) {
                Log.v(TAG, "no birthDay");
                finish();
                return;
            }
        }
        setBirthDay(date);
        setAge(calcAge(date));
    }

    static class ChooseState {
        Date birthDay = null;
    }

    private void chooseBirthDay()
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -17);
        final ChooseState state = new ChooseState();
        DatePickerDialog d = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(
                        DatePicker view,
                        int year,
                        int monthOfYear,
                        int dayOfMonth)
                    {
                        state.birthDay = new GregorianCalendar(year,
                            monthOfYear, dayOfMonth).getTime();
                    }
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH))
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                super.onClick(dialog, which);
                applyBirthDay(state.birthDay);
            }
        };
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                applyBirthDay(null);
            }
        });
        d.setCancelable(true);
        d.show();
    }
}
