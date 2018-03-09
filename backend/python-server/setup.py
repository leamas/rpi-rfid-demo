"""ddupdate install data."""

import shutil
import os
import subprocess

from distutils.command.clean import clean
from setuptools import setup

ROOT = os.path.dirname(__file__)
ROOT = ROOT if ROOT else '.'


def systemd_unitdir():
    """Return the official systemd unit dir path."""
    cmd = 'pkg-config systemd --variable=systemdsystemunitdir'.split()
    try:
        return subprocess.check_output(cmd).decode().strip()
    except (OSError, subprocess.CalledProcessError):
        return "/lib/systemd/system"  # The Raspbian default (old systemd).


setup(
    name='rfidevents',
    version='0.0.1',
    description='Exposes RFID HTML5 server events in tcp socket',
    license='MIT',

    long_description=open('README.md').read(),
    author='Alec Leamas',
    author_email='leamas@nowhere.net',
    classifiers=[
        'Development Status :: 2 - Pre-Alpha',
        'Topic :: System :: Networking',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: MIT License',
        'Programming Language :: Python :: 3.4',
    ],
    keywords=['rfid', 'tcp', 'html5'],
    install_requires=['pi-rc522'],

    packages=['rfidevents'],
    entry_points={
        'console_scripts': ['rfidserver = rfidserver:main']},
    data_files=[(systemd_unitdir(), ['rfidserver.service'])],
    zip_safe = False
)
