# CRM Android App

A mobile CRM application for sales teams and field agents. Built in Java for Android, it allows users to manage leads, deals, contacts, accounts, tasks, meetings, calls, visits, locations, and products on the go. Designed to work with the companion Laravel CRM Dashboard.

---

## Overview

**CRM Android App** provides mobile access to core CRM functions:

- Capture and convert leads
- Manage deals and accounts
- Log calls, meetings, visits, and tasks
- Track locations with Google Maps integration
- View and manage products and contacts
- Access a calendar and profile

The app communicates with the Laravel backend via REST API using Retrofit.

---

## Key Features

### Sales Pipeline
- **Leads** – Add, edit, view, and convert leads
- **Deals** – Full deal management
- **Accounts** – Company/account records
- **Contacts** – Contact management (with device contacts access)

### Activities
- **Calls** – Log and track calls
- **Meetings** – Schedule and manage meetings
- **Visits** – Record field visits
- **Tasks** – Create and manage tasks

### Other Modules
- **Locations** – Add and view locations with Google Maps & Places
- **Products** – Product catalog
- **Calendar** – Activity calendar view
- **Profile** – User profile management

### Navigation
- Bottom navigation with Home, Contacts, Deals, Locations, and More fragments
- Material Design UI with View Binding

---

## Tech Stack

| Component          | Technology                          |
|--------------------|-------------------------------------|
| Language           | Java                                |
| Min SDK            | 27                                  |
| Target SDK         | 34                                  |
| Networking         | Retrofit 2 + Gson                   |
| Image Loading      | Picasso                             |
| Maps               | Google Maps + Places SDK            |
| Security           | Android Security Crypto             |
| UI                 | Material Design · Navigation Component · View Binding |
| Local Storage      | Secure shared preferences           |

---

## Project Structure
