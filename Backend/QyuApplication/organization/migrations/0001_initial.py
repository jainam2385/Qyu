from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='OrganizationDetail',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('email', models.EmailField(max_length=100, unique=True)),
                ('password', models.CharField(max_length=100)),
                ('name', models.CharField(max_length=100)),
                ('address', models.CharField(max_length=500)),
                ('contact', models.CharField(blank=True, default='', max_length=10)),
                ('rating', models.FloatField(default=10.0)),
                ('is_verified', models.BooleanField(default=False)),
            ],
        ),
    ]
