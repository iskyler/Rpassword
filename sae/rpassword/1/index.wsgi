import os
import sys

import sae
from app import app

root = os.path.dirname(__file__)
sys.path.insert(0, os.path.join(root, 'site-packages.zip'))

application = sae.create_wsgi_app(app)
