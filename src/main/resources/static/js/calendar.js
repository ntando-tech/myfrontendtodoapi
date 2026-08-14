 let currentDate = new Date();


    function renderCalendar() {

        const year = currentDate.getFullYear();

        const month = currentDate.getMonth();


        const monthNames = [
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        ];


        document.getElementById("monthYear").innerText =
            monthNames[month] + " " + year;


        const firstDay =
            new Date(year, month, 1).getDay();


        const daysInMonth =
            new Date(year, month + 1, 0).getDate();


        const calendarDays =
            document.getElementById("calendarDays");


        calendarDays.innerHTML = "";


        // Empty spaces before first day

        for (let i = 0; i < firstDay; i++) {

            const emptyDay =
                document.createElement("div");

            emptyDay.classList.add(
                "day",
                "empty-day"
            );

            calendarDays.appendChild(emptyDay);
        }


        // Create calendar days

        for (let day = 1; day <= daysInMonth; day++) {

            const dayElement =
                document.createElement("div");

            dayElement.classList.add("day");


            const dayNumber =
                document.createElement("div");

            dayNumber.classList.add(
                "day-number"
            );

            dayNumber.innerText = day;


            dayElement.appendChild(dayNumber);


            // Create date string

            const monthString =
                String(month + 1).padStart(2, "0");

            const dayString =
                String(day).padStart(2, "0");


            const dateString =
                `${year}-${monthString}-${dayString}`;


            // Check if today

            const today = new Date();

            const todayString =
                `${today.getFullYear()}-${String(
                    today.getMonth() + 1
                ).padStart(2, "0")}-${String(
                    today.getDate()
                ).padStart(2, "0")}`;


            if (dateString === todayString) {

                dayElement.classList.add(
                    "today"
                );

            }


            // Find tasks belonging to this date

            tasks.forEach(function(task) {

                if (task.dueDate === dateString) {

                    const taskElement =
                        document.createElement("div");

                    taskElement.classList.add(
                        "task"
                    );

                    if((task.title).length < 12){
                        taskElement.innerText =
                        task.title;
                    }else{
                    taskElement.innerText =
                     task.title.substring(0,11);
                    }



                    taskElement.onclick =
                        function() {

                            showTask(task);

                        };


                    dayElement.appendChild(
                        taskElement
                    );

                }

            });


            calendarDays.appendChild(
                dayElement
            );

        }

    }


    function previousMonth() {

        currentDate.setMonth(
            currentDate.getMonth() - 1
        );

        renderCalendar();

    }


    function nextMonth() {

        currentDate.setMonth(
            currentDate.getMonth() + 1
        );

        renderCalendar();

    }


    function goToToday() {

        currentDate = new Date();

        renderCalendar();

    }


    function showTask(task) {

        document.getElementById(
            "modalTitle"
        ).innerText = task.title;


        document.getElementById(
            "modalDescription"
        ).innerText =
            task.description || "No description";


        document.getElementById(
            "modalDate"
        ).innerText =
            task.dueDate;

        document.getElementById(
            "modalPriority"
        ).innerText =
            task.priority;

        document.getElementById(
            "taskModal"
        ).style.display = "flex";

    }


    function closeCalendarModal() {

        document.getElementById(
            "taskModal"
        ).style.display = "none";

    }


    window.onclick = function(event) {

        const calendarModal =
            document.getElementById("taskModal");

        if (event.target === calendarModal) {

            closeCalendarModal();

        }

    };


    renderCalendar();