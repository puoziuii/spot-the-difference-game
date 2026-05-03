console.log("game.js charge - Version finale");

var markers = [];
var gameCompleteFlag = false;
var nextGameData = null;

document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM charge");
    
    var img = document.getElementById('image2');
    if (!img) {
        console.error("Image 2 non trouvee!");
        return;
    }
    
    initCanvas();
    
    window.addEventListener('resize', function() {
        redrawAllMarkers();
    });
    
    img.onload = function() {
        console.log("Image chargee - Taille reelle: " + img.naturalWidth + "x" + img.naturalHeight);
        redrawAllMarkers();
    };
    
    console.log("Image trouvee, ajout du clic...");
    
    img.onclick = function(e) {
        if (!window.gameActive || gameCompleteFlag) {
            console.log("Jeu inactif ou complete");
            return;
        }
        
        console.log("CLIC DETECTE!");
        
        var rect = this.getBoundingClientRect();
        var scaleX = this.naturalWidth / rect.width;
        var scaleY = this.naturalHeight / rect.height;
        
        var x = Math.round((e.clientX - rect.left) * scaleX);
        var y = Math.round((e.clientY - rect.top) * scaleY);
        
        console.log("Coordonnees image: " + x + "," + y);
        
        fetch('api/check-difference', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'x=' + x + '&y=' + y
        })
        .then(function(r) { 
            console.log("Reponse status:", r.status);
            return r.text(); 
        })
        .then(function(data) {
            console.log("Reponse data:", data);
            
            if (data.startsWith('success')) {
                var parts = data.split(':');
                var foundCount = parseInt(parts[1]);
                
                markers.push({x: x, y: y});
                redrawAllMarkers();
                
                var foundSpan = document.getElementById('foundCount');
                if (foundSpan) {
                    foundSpan.innerText = foundCount;
                }
                
                showMessage("Trouve! (" + foundCount + "/" + window.totalDifferences + ")", 'success');
                
                if (foundCount >= window.totalDifferences) {
                    gameCompleteFlag = true;
                    window.gameActive = false;
                    if (window.timerInterval) {
                        clearInterval(window.timerInterval);
                    }
                    showNextGameButton();
                }
                
            } else if (data.startsWith('complete')) {
                var parts = data.split(':');
                var foundCount = parseInt(parts[1]);
                var points = parseInt(parts[2]);
                var hasNextGame = parts[3] === 'true';
                var hasNextLevel = parts[4] === 'true';
                var isLastGame = parts[5] === 'true';
                
                markers.push({x: x, y: y});
                redrawAllMarkers();
                
                document.getElementById('foundCount').innerText = foundCount;
                showMessage("Game Complete! +" + points + " points!", 'success');
                
                window.gameActive = false;
                gameCompleteFlag = true;
                if (window.timerInterval) {
                    clearInterval(window.timerInterval);
                }
                
                nextGameData = {
                    hasNextGame: hasNextGame,
                    hasNextLevel: hasNextLevel,
                    isLastGame: isLastGame
                };
                
                showNextGameButton();
                
            } else if (data === 'already_found') {
                showMessage("⚠️ Vous avez déjà trouvé cette différence!", 'warning');
                
            } else if (data === 'fail') {
                showMessage("Pas une difference!", 'error');
            }
        })
        .catch(function(err) { 
            console.error("Erreur:", err);
            showMessage("Erreur de connexion!", 'error');
        });
    };
    
    console.log("Clic ajoute avec succes!");
    startTimer();
});

function initCanvas() {
    var canvas = document.getElementById('differenceCanvas');
    var img = document.getElementById('image2');
    if (!canvas || !img) return;
    
    var rect = img.getBoundingClientRect();
    canvas.width = rect.width;
    canvas.height = rect.height;
    canvas.style.width = rect.width + 'px';
    canvas.style.height = rect.height + 'px';
}

function redrawAllMarkers() {
    var canvas = document.getElementById('differenceCanvas');
    var img = document.getElementById('image2');
    if (!canvas || !img) return;
    
    var rect = img.getBoundingClientRect();
    var scaleX = rect.width / img.naturalWidth;
    var scaleY = rect.height / img.naturalHeight;
    
    canvas.width = rect.width;
    canvas.height = rect.height;
    canvas.style.width = rect.width + 'px';
    canvas.style.height = rect.height + 'px';
    
    var ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    for (var i = 0; i < markers.length; i++) {
        var marker = markers[i];
        var cx = marker.x * scaleX;
        var cy = marker.y * scaleY;
        
        ctx.beginPath();
        ctx.arc(cx, cy, 20, 0, 2 * Math.PI);
        ctx.fillStyle = 'rgba(76,175,80,0.6)';
        ctx.fill();
        ctx.fillStyle = 'white';
        ctx.font = '24px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText('✓', cx, cy);
    }
}

var timeLeft = 60;
var timerInterval = null;

function startTimer() {
    var timeInput = document.getElementById('timeLimit');
    if (timeInput && timeInput.value) {
        timeLeft = parseInt(timeInput.value);
    }
    
    var totalInput = document.getElementById('totalDifferences');
    if (totalInput && totalInput.value) {
        window.totalDifferences = parseInt(totalInput.value);
    }
    
    var timerEl = document.getElementById('timer');
    if (!timerEl) {
        console.error("Timer element non trouve");
        return;
    }
    
    console.log("Timer demarre avec " + timeLeft + " secondes");
    
    window.gameActive = true;
    gameCompleteFlag = false;
    
    if (timerInterval) {
        clearInterval(timerInterval);
    }
    
    timerInterval = setInterval(function() {
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            window.gameActive = false;
            showMessage("Time is up! Game over.", 'error');
            setTimeout(function() { location.reload(); }, 2000);
        } else if (!gameCompleteFlag) {
            timeLeft--;
            timerEl.innerText = timeLeft;
            if (timeLeft <= 10) {
                timerEl.style.color = '#ff4444';
                timerEl.style.fontWeight = 'bold';
            }
        }
    }, 1000);
    
    window.timerInterval = timerInterval;
}

function showNextGameButton() {
    var nextBtn = document.getElementById('nextGameBtn');
    if (nextBtn) {
        nextBtn.style.display = 'inline-block';
        console.log("✅ Next Game button shown");
        
        // Scroll to button
        nextBtn.scrollIntoView({ behavior: 'smooth', block: 'center' });
    } else {
        console.error("❌ Next Game button not found in DOM!");
    }
}

function hideNextGameButton() {
    var nextBtn = document.getElementById('nextGameBtn');
    if (nextBtn) {
        nextBtn.style.display = 'none';
        console.log("❌ Next Game button hidden");
    }
}

function goToNextGame() {
    console.log("➡️ Go to next game clicked");
    hideNextGameButton();
    showMessage("Loading next game...", 'success');
    
    console.log("📡 Sending request to: api/next-game");
    
    fetch('api/next-game', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function(response) {
        console.log("📡 Response status:", response.status);
        if (!response.ok) {
            throw new Error('HTTP error ' + response.status);
        }
        return response.json();
    })
    .then(function(data) {
        console.log("📦 Next game response:", data);
        
        if (data.success) {
            if (data.type === 'nextGame') {
                showMessage("🎮 Moving to Game " + data.newGameNumber + " of Level " + data.level + "!", 'success');
            } else if (data.type === 'nextLevel') {
                showMessage("🎉 Moving to Level " + data.newLevel + "! 🎉", 'success');
            }
            setTimeout(function() {
                location.reload();
            }, 800);
        } else if (data.completed) {
            showMessage("🏆 Congratulations! You completed all levels! 🏆", 'success');
            setTimeout(function() {
                window.location.href = 'win';
            }, 1500);
        } else if (data.error) {
            showMessage("Error: " + data.error, 'error');
        }
    })
    .catch(function(error) {
        console.error("❌ Error in next game:", error);
        showMessage("Error moving to next game. Please refresh.", 'error');
    });
}

function resetGame() {
    if (confirm('⚠️ Are you sure? All your progress will be lost!')) {
        console.log("🔄 Resetting game...");
        
        fetch('api/reset-game', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            console.log("Reset response:", data);
            if (data.success) {
                // الانتظار قليلاً ثم التوجيه إلى الصفحة الرئيسية
                setTimeout(function() {
                    window.location.href = 'game';
                }, 500);
            } else {
                showMessage("Error resetting game: " + data.error, 'error');
                // إذا فشل، حاول التوجيه مباشرة
                setTimeout(function() {
                    window.location.href = 'game';
                }, 1000);
            }
        })
        .catch(function(error) {
            console.error("Reset error:", error);
            showMessage("Error resetting game. Please refresh the page.", 'error');
            setTimeout(function() {
                window.location.href = 'game';
            }, 1500);
        });
    }
}

function showMessage(msg, type) {
    var area = document.getElementById('messageArea');
    if (!area) return;
    
    area.innerHTML = '<div class="message ' + type + '">' + msg + '</div>';
    area.style.display = 'block';
    
    setTimeout(function() {
        area.style.display = 'none';
    }, 2500);
}