import os
import sys

root = os.path.dirname(__file__)
sys.path.insert(0, os.path.join(root, 'virtualenv.bundle.zip'))

import sae
from app import app, db

application = sae.create_wsgi_app(app)
